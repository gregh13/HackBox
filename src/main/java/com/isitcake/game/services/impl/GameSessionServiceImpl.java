package com.isitcake.game.services.impl;

import com.isitcake.game.entities.GameSession;
import com.isitcake.game.entities.Player;
import com.isitcake.game.entities.dtos.GameSessionDto;
import com.isitcake.game.enums.StateType;
import com.isitcake.game.mappers.GameSessionMapper;
import com.isitcake.game.repositories.GameSessionRepository;
import com.isitcake.game.services.GameSessionService;
import com.isitcake.game.services.PlayerService;
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
    @Transactional
    public GameSession updateGameStateAndPlayers(String sessionId, StateType gameState) {
        GameSession gameSession = getGameSession(sessionId);
        if (gameSession == null) {
            return null;
        }
        List<Player> players = gameSession.getPlayers();
        if (gameState.equals(StateType.QUESTION)){
            players = playerService.resetPlayerChoices(players);
        }
        if (gameState.equals(StateType.SETUP)) {
            players = playerService.removeInactivePlayers(players);
        }

        gameSession.setGameState(gameState);
        gameSession.setPlayers(players);
        return gameSessionRepository.saveAndFlush(gameSession);

    }

    @Override
    public GameSession getGameSession(String sessionId) {
        Optional<GameSession> optionalGameSession = gameSessionRepository.findBySessionId(sessionId);
        if (optionalGameSession.isEmpty()) {
            // TODO: Add exception here
            System.out.println("Error: Optional Game Session is empty");
            return null;
        }
        return optionalGameSession.get();
    }

    @Override
    public GameSessionDto getGameSessionDto(GameSession gameSession) {
        if (gameSession == null) {
            return null;
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
    public GameSession joinGameSession(String playerName, String sessionId) {
        Optional<GameSession> optionalGameSession = gameSessionRepository.findBySessionId(sessionId);
        if (optionalGameSession.isEmpty()) {
            // TODO: add exception here
            System.out.println("Error: Game session '" + sessionId + "' could not be found.");
            return null;
        }

        GameSession gameSession = optionalGameSession.get();
        Player newPlayer = playerService.createPlayer(playerName, gameSession, false);
        if (newPlayer == null) {
            // TODO: add exception here
            return null;
        }

        gameSession.getPlayers().add(newPlayer);
        return gameSessionRepository.saveAndFlush(gameSession);
    }

    @Override
    @Transactional
    public void removeStaleGames() {
        gameSessionRepository.deleteAllOldSessions(new Timestamp(System.currentTimeMillis()));
    }
}
