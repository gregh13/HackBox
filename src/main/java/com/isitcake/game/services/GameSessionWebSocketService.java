package com.isitcake.game.services;

import com.isitcake.game.entities.GameSession;

public interface GameSessionWebSocketService {
    void broadcastGameState(GameSession gameSession);
}

