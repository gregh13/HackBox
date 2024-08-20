package com.isitcake.game.service.impl;

import com.isitcake.game.dto.response.PlayerResponseDto;
import com.isitcake.game.entity.GameSession;
import com.isitcake.game.entity.Player;
import com.isitcake.game.exception.NullEntityException;
import com.isitcake.game.exception.PlayerNameTakenException;
import com.isitcake.game.exception.PlayerNotFoundException;
import com.isitcake.game.mapper.PlayerMapper;
import com.isitcake.game.repository.PlayerRepository;
import com.isitcake.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PlayerServiceImpl implements PlayerService {
    PlayerRepository playerRepository;
    PlayerMapper playerMapper;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository, PlayerMapper playerMapper) {
        this.playerRepository = playerRepository;
        this.playerMapper = playerMapper;
    }

    @Override
    public Player createPlayer(String playerName, GameSession gameSession, Boolean isHost) {
        for (Player player : gameSession.getPlayers() ) {
            if (player.getName().equals(playerName)) {
                String message = "The player name already exists in the game session and cannot be used again. Please try a new name to join the session.";
                throw new PlayerNameTakenException(message);
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
            String message = "Player '" + playerName + "' could not be found";
            throw new PlayerNotFoundException(message);
        }

        Player player = playerOpt.get();
        player.setChoice(choice);
        player.setTimeTaken(timeTaken);
        return playerRepository.saveAndFlush(player);
    }

    @Override
    public PlayerResponseDto getPlayerDto(Player player) {
        if (player == null) {
            String message = "Player is null, cannot create PlayerDto";
            throw new NullEntityException(message);
        }
        return playerMapper.entityToDto(player);
    }

    @Override
    public List<PlayerResponseDto> getPlayerDtos(List<Player> players) {
        if (players == null) {
            System.out.println("Players are null, creating new ArrayList");
            players = new ArrayList<Player>();
        }
        return playerMapper.entitiesToDtos(players);
    }
}
