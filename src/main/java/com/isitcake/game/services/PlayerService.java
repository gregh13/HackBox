package com.isitcake.game.services;

import com.isitcake.game.entities.GameSession;
import com.isitcake.game.entities.Player;
import com.isitcake.game.entities.dtos.PlayerDto;

import java.util.List;

public interface PlayerService {
    Player createPlayer(String playerName, GameSession gameSession, Boolean isHost);

    Player updatePlayer(String playerName, List<Player> gameSessionPlayers, String choice, Double timeTaken);

    PlayerDto getPlayerDto(Player player);
    List<PlayerDto> getPlayerDtos(List<Player> players);
}
