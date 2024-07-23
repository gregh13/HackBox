package com.isitcake.game.services;

import com.isitcake.game.entities.GameSession;

import java.sql.Timestamp;
import java.util.Optional;

public interface GameSessionService {
    Optional<GameSession> findBySessionId(String sessionId);
    GameSession createGameSession(int season, int episodeNumber);
    GameSession updateGameState(String sessionId, String state);
    String getGameState(String sessionId);
    GameSession pauseGameSession(String sessionId);
    GameSession resumeGameSession(String sessionId);
    GameSession updateEpisodeStartTime(String sessionId, Timestamp newEpisodeStartTime);
    void updateAllGameSessions();
}
