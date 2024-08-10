package com.isitcake.game.services;


import com.isitcake.game.entities.GameSession;
import com.isitcake.game.entities.Player;
import com.isitcake.game.entities.dtos.GameSessionDto;
import com.isitcake.game.enums.StateType;

import java.util.List;

public interface GameSessionService {
    GameSession updateGameStateAndPlayers(String sessionId, StateType gameState);
    GameSession getGameSession(String sessionId);
    GameSessionDto getGameSessionDto(GameSession gameSession);

    List<Player> getPlayersBySessionId(String sessionId);

    GameSession createGameSession(String playerName);

    GameSession joinGameSession(String playerName, String sessionId);

    void removeStaleGames();
}
