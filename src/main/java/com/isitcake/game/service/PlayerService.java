package com.isitcake.game.service;

import com.isitcake.game.dto.response.PlayerResponseDto;
import com.isitcake.game.entity.GameSession;
import com.isitcake.game.entity.Player;

import java.util.List;

public interface PlayerService {
    Player createPlayer(String playerName, GameSession gameSession, Boolean isHost);

    Player updatePlayer(String playerName, List<Player> gameSessionPlayers, String choice, Double timeTaken);

    PlayerResponseDto getPlayerDto(Player player);
    List<PlayerResponseDto> getPlayerDtos(List<Player> players);
}
