package com.isitcake.game.service.impl;

import com.isitcake.game.entity.GameSession;
import com.isitcake.game.entity.Player;
import com.isitcake.game.dto.response.GameSessionResponseDto;
import com.isitcake.game.exception.GameSessionNotFoundException;
import com.isitcake.game.exception.NullEntityException;
import com.isitcake.game.exception.PlayerNotFoundException;
import com.isitcake.game.type.StateType;
import com.isitcake.game.mapper.GameSessionMapper;
import com.isitcake.game.repository.GameSessionRepository;
import com.isitcake.game.service.GameSessionService;
import com.isitcake.game.service.PlayerService;
import com.isitcake.game.util.SessionIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GameSessionServiceImpl implements GameSessionService {
    @Autowired
    GameSessionRepository gameSessionRepository;

    @Autowired
    PlayerService playerService;

    @Autowired
    GameSessionMapper gameSessionMapper;

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
    @Transactional(readOnly = true)
    public List<Player> getPlayersBySessionId(String sessionId) {
        Optional<GameSession> optionalGameSession = gameSessionRepository.findBySessionId(sessionId);
        if (optionalGameSession.isEmpty()){
            System.out.println("Game session optional is empty");
            return new ArrayList<Player>();
        }
        GameSession gameSession = optionalGameSession.get();
        System.out.println("Game session object found: " + gameSession);
        List<Player> players = gameSession.getPlayers();
        System.out.println("Game session players list: " + players);
        return players;
    }

    @Override
    @Transactional
    public GameSession createGameSession(String playerName) {
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
        return gameSessionRepository.saveAndFlush(gameSession);
    }


    @Override
    @Transactional
    public GameSession joinGameSession(String playerName, String sessionId) {
        GameSession gameSession = getGameSession(sessionId);
        Player newPlayer = playerService.createPlayer(playerName, gameSession, false);
        gameSession.getPlayers().add(newPlayer);
        return gameSessionRepository.saveAndFlush(gameSession);
    }

    @Override
    @Transactional
    public GameSession updateGameState(String sessionId, StateType gameState) {
        GameSession gameSession = getGameSession(sessionId);
        gameSession.setGameState(gameState);
        return gameSessionRepository.saveAndFlush(gameSession);
    }
}
