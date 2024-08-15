package com.isitcake.game.service;


import com.isitcake.game.entity.GameSession;
import com.isitcake.game.entity.Player;
import com.isitcake.game.dto.response.GameSessionResponseDto;
import com.isitcake.game.type.StateType;

import java.util.List;

public interface GameSessionService {
    GameSession updateGameState(String sessionId, StateType gameState);
    GameSession getGameSession(String sessionId);
    GameSessionResponseDto getGameSessionDto(GameSession gameSession);

    List<Player> getPlayersBySessionId(String sessionId);

    GameSession createGameSession(String playerName);

    GameSession joinGameSession(String playerName, String sessionId);
}
