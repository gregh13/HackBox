package com.isitcake.game.services;


import com.isitcake.game.entities.GameSession;
import com.isitcake.game.entities.dtos.GameSessionDto;
import com.isitcake.game.enums.StateType;

public interface GameSessionService {
    GameSession updateGameState(String sessionId, StateType gameState);
    GameSession getGameSession(String sessionId);
    GameSessionDto getGameSessionDto(GameSession gameSession);

    GameSession createGameSession(String playerName);

    GameSession joinGameSession(String playerName, String sessionId);
}
