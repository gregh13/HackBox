package com.isitcake.game.services;

import com.isitcake.game.entities.GameSession;
import com.isitcake.game.entities.Player;

public interface PlayerService {
    Player createPlayer(String playerName, GameSession gameSession, Boolean isHost);

    Player updatePlayer(String playerName, GameSession gameSession, String choice, Double timeTaken);
}
