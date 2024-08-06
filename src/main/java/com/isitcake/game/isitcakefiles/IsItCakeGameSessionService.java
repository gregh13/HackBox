package com.isitcake.game.isitcakefiles;

import com.isitcake.game.isitcakefiles.IsItCakeGameSession;

import java.sql.Timestamp;
import java.util.Optional;

public interface IsItCakeGameSessionService {
    Optional<IsItCakeGameSession> findBySessionId(String sessionId);
    IsItCakeGameSession createGameSession(int season, int episodeNumber, String playerName);
    IsItCakeGameSession updateGameState(String sessionId, String state);
    String getGameState(String sessionId);
    IsItCakeGameSession pauseGameSession(String sessionId, String playerName);
    IsItCakeGameSession resumeGameSession(String sessionId, String playerName);
    IsItCakeGameSession updateEpisodeStartTime(String sessionId, String playerName, Timestamp newEpisodeStartTime);
    void updateAllGameSessions();
    IsItCakeGameSession addPlayer(String sessionId, String playerName);
    IsItCakeGameSession recordPlayerAnswer(String sessionId, String playerName, int selectedChoice, long answerTime);
}
