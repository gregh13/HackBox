package com.isitcake.game.services.impl;

import com.isitcake.game.entities.GameSession;
import com.isitcake.game.entities.Episode;
import com.isitcake.game.repositories.EpisodeRepository;
import com.isitcake.game.repositories.GameSessionRepository;
import com.isitcake.game.services.GameSessionService;
import com.isitcake.game.services.GameSessionWebSocketService;
import com.isitcake.game.util.SessionIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class GameSessionServiceImpl implements GameSessionService {

    @Autowired
    private GameSessionRepository gameSessionRepository;

    @Autowired
    private EpisodeRepository episodeRepository;

    @Autowired
    private GameSessionWebSocketService gameSessionWebSocketService;

    @Override
    public Optional<GameSession> findBySessionId(String sessionId) {
        return gameSessionRepository.findBySessionId(sessionId);
    }

    @Override
    public GameSession createGameSession(int season, int episodeNumber) {
        Optional<Episode> episodeOptional = episodeRepository.findBySeasonAndEpisodeNumber(season, episodeNumber);
        if (episodeOptional.isPresent()) {
            Episode episode = episodeOptional.get();
            String sessionId = SessionIdGenerator.generateSessionId();
            GameSession gameSession = new GameSession();
            gameSession.setSessionId(sessionId);
            gameSession.setCurrentState("initial");
            gameSession.setEpisodeStartTime(new Timestamp(System.currentTimeMillis()));
            gameSession.setPausedStartTime(null);
            gameSession.setIsPaused(false);
            gameSession.setIsActive(true); // Mark as active
            gameSession.setEpisode(episode);
            gameSession = gameSessionRepository.save(gameSession);
            gameSessionWebSocketService.broadcastGameState(gameSession);
            return gameSession;
        } else {
            throw new RuntimeException("Episode not found");
        }
    }

    @Override
    public GameSession updateGameState(String sessionId, String newState) {
        Optional<GameSession> gameSessionOptional = gameSessionRepository.findBySessionId(sessionId);
        if (gameSessionOptional.isPresent()) {
            GameSession gameSession = gameSessionOptional.get();
            gameSession.setCurrentState(newState);
            gameSession = gameSessionRepository.save(gameSession);
            gameSessionWebSocketService.broadcastGameState(gameSession);
            return gameSession;
        } else {
            throw new RuntimeException("Game session not found");
        }
    }

    @Override
    public String getGameState(String sessionId) {
        Optional<GameSession> gameSessionOptional = gameSessionRepository.findBySessionId(sessionId);
        return gameSessionOptional.map(GameSession::getCurrentState).orElse("not found");
    }

    @Override
    public GameSession pauseGameSession(String sessionId) {
        Optional<GameSession> gameSessionOptional = gameSessionRepository.findBySessionId(sessionId);
        if (gameSessionOptional.isPresent()) {
            GameSession gameSession = gameSessionOptional.get();
            gameSession.setPausedStartTime(new Timestamp(System.currentTimeMillis()));
            gameSession.setIsPaused(true);
            gameSession = gameSessionRepository.save(gameSession);
            gameSessionWebSocketService.broadcastGameState(gameSession);
            return gameSession;
        } else {
            throw new RuntimeException("Game session not found");
        }
    }

    @Override
    public GameSession resumeGameSession(String sessionId) {
        Optional<GameSession> gameSessionOptional = gameSessionRepository.findBySessionId(sessionId);
        if (gameSessionOptional.isPresent()) {
            GameSession gameSession = gameSessionOptional.get();
            if (gameSession.getIsPaused()) {
                long pausedDuration = System.currentTimeMillis() - gameSession.getPausedStartTime().getTime();
                Timestamp newEpisodeStartTime = new Timestamp(gameSession.getEpisodeStartTime().getTime() + pausedDuration);
                gameSession.setEpisodeStartTime(newEpisodeStartTime);
                gameSession.setPausedStartTime(null);
                gameSession.setIsPaused(false);
                gameSession = gameSessionRepository.save(gameSession);
                gameSessionWebSocketService.broadcastGameState(gameSession);
                return gameSession;
            }
            return gameSession;
        } else {
            throw new RuntimeException("Game session not found");
        }
    }

    @Override
    public GameSession updateEpisodeStartTime(String sessionId, Timestamp newEpisodeStartTime) {
        Optional<GameSession> gameSessionOptional = gameSessionRepository.findBySessionId(sessionId);
        if (gameSessionOptional.isPresent()) {
            GameSession gameSession = gameSessionOptional.get();
            gameSession.setEpisodeStartTime(newEpisodeStartTime);
            gameSession = gameSessionRepository.save(gameSession);
            gameSessionWebSocketService.broadcastGameState(gameSession);
            return gameSession;
        } else {
            throw new RuntimeException("Game session not found");
        }
    }

    @Override
    public void updateAllGameSessions() {
        List<GameSession> activeSessions = gameSessionRepository.findAllActiveSessions();
        for (GameSession session : activeSessions) {
            long elapsedTime = System.currentTimeMillis() - session.getEpisodeStartTime().getTime();
            updateGameStateBasedOnTime(session, elapsedTime);
            gameSessionWebSocketService.broadcastGameState(session);
        }
    }

    private void updateGameStateBasedOnTime(GameSession session, long elapsedTime) {
        if (session.getCurrentState().equals("initial") && elapsedTime > 5000) { // 5 seconds for demo
            session.setCurrentState("question_1");
        }
        // Add more logic to transition between states based on elapsed time
        gameSessionRepository.save(session);
    }
}
