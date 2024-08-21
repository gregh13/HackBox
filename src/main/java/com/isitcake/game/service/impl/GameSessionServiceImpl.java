package com.isitcake.game.service.impl;

import com.isitcake.game.dto.response.PlayerResponseDto;
import com.isitcake.game.entity.GameSession;
import com.isitcake.game.entity.Player;
import com.isitcake.game.dto.response.GameSessionResponseDto;
import com.isitcake.game.exception.GameSessionNotFoundException;
import com.isitcake.game.exception.NullEntityException;
import com.isitcake.game.type.StateType;
import com.isitcake.game.mapper.GameSessionMapper;
import com.isitcake.game.repository.GameSessionRepository;
import com.isitcake.game.service.GameSessionService;
import com.isitcake.game.service.PlayerService;
import com.isitcake.game.util.QuestionIdGenerator;
import com.isitcake.game.util.SessionIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GameSessionServiceImpl implements GameSessionService {
    GameSessionRepository gameSessionRepository;
    PlayerService playerService;
    GameSessionMapper gameSessionMapper;

    @Autowired
    public GameSessionServiceImpl(GameSessionRepository gameSessionRepository, PlayerService playerService, GameSessionMapper gameSessionMapper) {
        this.gameSessionRepository = gameSessionRepository;
        this.playerService = playerService;
        this.gameSessionMapper = gameSessionMapper;
    }

    @Override
    public GameSession getGameSession(String sessionId) {
        Optional<GameSession> optionalGameSession = gameSessionRepository.findBySessionId(sessionId);
        if (optionalGameSession.isEmpty()) {
            String message = "Could not find game session with id: " + sessionId;
            throw new GameSessionNotFoundException(message);
        }
        return optionalGameSession.get();
    }

    @Override
    public GameSessionResponseDto getGameSessionDto(GameSession gameSession) {
        if (gameSession == null) {
            String message = "Game session is null, cannot create gameSessionDto";
            throw new NullEntityException(message);
        }
        return gameSessionMapper.entityToDto(gameSession);
    }

    @Override
    public GameSessionResponseDto getRequestGameSession(String sessionId) {
        return getGameSessionDto(getGameSession(sessionId));
    }

    @Override
    public List<PlayerResponseDto> getCurrentPlayers(String sessionId) {
        GameSession gameSession = getGameSession(sessionId);
        return playerService.getPlayerDtos(gameSession.getPlayers());
    }

    @Override
    public GameSessionResponseDto createGameSession(String playerName) {
        String gameSessionId;
        Optional<GameSession> existingGameSession;

        do {
            gameSessionId = SessionIdGenerator.generateSessionId();
            existingGameSession = gameSessionRepository.findBySessionId(gameSessionId);
        }
        while (existingGameSession.isPresent());

        // Unique sessionId found, proceed with creating new Game Session
        GameSession newGameSession = new GameSession();
        newGameSession.setSessionId(gameSessionId);
        newGameSession.setGameState(StateType.SETUP);
        newGameSession.setActive(true);
        newGameSession.setDateCreated(new Timestamp(System.currentTimeMillis()));
        GameSession gameSession = gameSessionRepository.saveAndFlush(newGameSession);

        // Create the host player and add to GameSession players
        Player playerOne = playerService.createPlayer(playerName, gameSession, true);
        List<Player> players = gameSession.getPlayers();
        players.add(playerOne);
        gameSession.setPlayers(players);
        return getGameSessionDto(gameSessionRepository.saveAndFlush(gameSession));
    }


    @Override
    public GameSessionResponseDto joinGameSession(String playerName, String sessionId) {
        GameSession gameSession = getGameSession(sessionId);
        Player newPlayer = playerService.createPlayer(playerName, gameSession, false);
        gameSession.getPlayers().add(newPlayer);
        return getGameSessionDto(gameSessionRepository.saveAndFlush(gameSession));
    }

    @Override
    public GameSessionResponseDto transferHost(String sessionId, String playerName) {
        GameSession gameSession = getGameSession(sessionId);
        List<Player> players = playerService.transferHost(sessionId, playerName);
        gameSession.setPlayers(players);
        return getGameSessionDto(gameSessionRepository.saveAndFlush(gameSession));
    }

    @Override
    public GameSessionResponseDto transitionToSetup(String sessionId, StateType gameState) {
        GameSession gameSession = getGameSession(sessionId);
        gameSession.setGameState(gameState);

        List<Player> players = gameSession.getPlayers();
        players = playerService.findActivePlayers(players, gameSession.getQuestionId());
        gameSession.setPlayers(players);

        return getGameSessionDto(gameSessionRepository.saveAndFlush(gameSession));
    }

    @Override
    public GameSessionResponseDto transitionToResults(String sessionId, StateType gameState) {
        GameSession gameSession = getGameSession(sessionId);
        gameSession.setGameState(gameState);
        return getGameSessionDto(gameSessionRepository.saveAndFlush(gameSession));
    }

    @Override
    public GameSessionResponseDto transitionToQuestion(String sessionId, StateType gameState) {
        GameSession gameSession = getGameSession(sessionId);
        gameSession.setGameState(gameState);

        String questionId = QuestionIdGenerator.generateQuestionId();

        List<Player> players = gameSession.getPlayers();
        players = playerService.resetPlayerAnswers(players, questionId);

        gameSession.setPlayers(players);

        return getGameSessionDto(gameSessionRepository.saveAndFlush(gameSession));
    }
}
