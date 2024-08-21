package com.isitcake.game.service;

import com.isitcake.game.dto.response.PlayerResponseDto;
import com.isitcake.game.entity.GameSession;
import com.isitcake.game.entity.Player;

import java.util.List;

public interface PlayerService {
    Player createPlayer(String playerName, GameSession gameSession, Boolean isHost);

    PlayerResponseDto updatePlayerAnswer(String sessionId, String playerName, String choice, Double timeTaken, String questionId);

    PlayerResponseDto getPlayerDto(Player player);
    List<PlayerResponseDto> getPlayerDtos(List<Player> players);

    List<Player> resetPlayerAnswers(List<Player> players, String questionId);

    List<Player> transferHost(String sessionId, String playerName);

    List<Player> findActivePlayers(List<Player> players, String questionId);

}
