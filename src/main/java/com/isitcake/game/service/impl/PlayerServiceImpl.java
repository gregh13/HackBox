package com.isitcake.game.service.impl;

import com.isitcake.game.dto.response.PlayerResponseDto;
import com.isitcake.game.entity.GameSession;
import com.isitcake.game.entity.Player;
import com.isitcake.game.mapper.PlayerMapper;
import com.isitcake.game.repository.PlayerRepository;
import com.isitcake.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PlayerServiceImpl implements PlayerService {
    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    PlayerMapper playerMapper;

    @Override
    public Player createPlayer(String playerName, GameSession gameSession, Boolean isHost) {
        for (Player player : gameSession.getPlayers() ) {
            if (player.getName().equals(playerName)) {
                // TODO: Add exceptions to replace console log
                System.out.println("Error: Player name '" + playerName + "' already in use for session '" + gameSession.getSessionId() + "'");
                return null;
            }
        }

        Player newPlayer = new Player();
        newPlayer.setName(playerName);
        newPlayer.setSessionId(gameSession.getSessionId());
        newPlayer.setGameSession(gameSession);
        newPlayer.setSessionHost(isHost);

        return playerRepository.saveAndFlush(newPlayer);
    }

    @Override
    public Player updatePlayer(String playerName, List<Player> gameSessionPlayers, String choice, Double timeTaken) {
        Optional<Player> playerOpt = gameSessionPlayers.stream()
                .filter(p -> p.getName().equals(playerName))
                .findFirst();

        if (playerOpt.isEmpty()) {
            // TODO: Add exception
            System.out.printf("Error: Player '%s' could not be found", playerName);
            return null;
        }

        Player player = playerOpt.get();
        player.setChoice(choice);
        player.setTimeTaken(timeTaken);
        return playerRepository.saveAndFlush(player);
    }

    @Override
    public PlayerResponseDto getPlayerDto(Player player) {
        if (player == null) {
            return null;
        }
        return playerMapper.entityToDto(player);
    }

    @Override
    public List<PlayerResponseDto> getPlayerDtos(List<Player> players) {
        if (players == null) {
            players = new ArrayList<Player>();
        }
        return playerMapper.entitiesToDtos(players);
    }
}
