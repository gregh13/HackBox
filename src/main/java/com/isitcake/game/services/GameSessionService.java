package com.isitcake.game.services;

import com.isitcake.game.entities.GameSession;

import java.sql.Timestamp;
import java.util.Optional;

public interface GameSessionService {
    Optional<GameSession> findBySessionId(String sessionId);
    GameSession createGameSession(int season, int episodeNumber, String playerName);
    GameSession updateGameState(String sessionId, String state);
    String getGameState(String sessionId);
    GameSession pauseGameSession(String sessionId, String playerName);
    GameSession resumeGameSession(String sessionId, String playerName);
    GameSession updateEpisodeStartTime(String sessionId, String playerName, Timestamp newEpisodeStartTime);
    void updateAllGameSessions();
    GameSession addPlayer(String sessionId, String playerName);
    GameSession recordPlayerAnswer(String sessionId, String playerName, int selectedChoice, long answerTime);
}
