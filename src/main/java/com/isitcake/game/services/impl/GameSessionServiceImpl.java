package com.isitcake.game.services.impl;

import com.isitcake.game.entities.GameSession;
import com.isitcake.game.entities.Player;
import com.isitcake.game.enums.StateType;
import com.isitcake.game.repositories.GameSessionRepository;
import com.isitcake.game.services.GameSessionService;
import com.isitcake.game.services.PlayerService;
import com.isitcake.game.util.SessionIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

@Service
public class GameSessionServiceImpl implements GameSessionService {
    @Autowired
    GameSessionRepository gameSessionRepository;

    @Autowired
    PlayerService playerService;

    @Override
    public GameSession updateGameState(String sessionId, StateType gameState) {
        GameSession gameSession = getGameSession(sessionId);
        if (gameSession == null) {
            return null;
        }
        gameSession.setGameState(gameState);
        return gameSessionRepository.saveAndFlush(gameSession);

    }

    @Override
    public GameSession getGameSession(String sessionId) {
        Optional<GameSession> optionalGameSession = gameSessionRepository.findBySessionId(sessionId);
        return optionalGameSession.orElse(null);
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
        gameSession.getPlayers().add(playerOne);
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
}
