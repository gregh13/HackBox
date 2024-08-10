package com.isitcake.game.services.impl;

import com.isitcake.game.entities.GameSession;
import com.isitcake.game.entities.Player;
import com.isitcake.game.entities.dtos.PlayerDto;
import com.isitcake.game.mappers.PlayerMapper;
import com.isitcake.game.repositories.PlayerRepository;
import com.isitcake.game.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Player updatePlayer(String sessionId, String playerName, String choice, Double timeTaken) {
        Optional<Player> playerOpt = playerRepository.findByNameAndSessionId(playerName, sessionId);

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
    public PlayerDto getPlayerDto(Player player) {
        if (player == null) {
            return null;
        }
        return playerMapper.entityToDto(player);
    }

    @Override
    public List<PlayerDto> getPlayerDtos(List<Player> players) {
        if (players == null) {
            players = new ArrayList<Player>();
        }
        return playerMapper.entitiesToDtos(players);
    }

    @Override
    public List<Player> resetPlayerChoices(List<Player> players) {
        for (Player player : players) {
            player.setChoice("");
            player.setTimeTaken(-1.0);
        }
        return playerRepository.saveAllAndFlush(players);
    }

    @Transactional
    @Override
    public List<PlayerDto> transferHost(String sessionId, String playerName) {
        // Fetch all players in the session
        List<Player> players = playerRepository.findAllBySessionId(sessionId);

        // Find the current host and the new host using streams
        Player oldHostPlayer = players.stream()
                .filter(Player::getSessionHost)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Old host player not found!"));

        Player newHostPlayer = players.stream()
                .filter(player -> player.getName().equals(playerName))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("New host player not found!"));

        // Update host flags
        oldHostPlayer.setSessionHost(false);
        newHostPlayer.setSessionHost(true);

        // Save both updated players and return the mapped DTOs
        return playerMapper.entitiesToDtos(playerRepository.saveAllAndFlush(players));
    }

    @Override
    public List<Player> removeInactivePlayers(List<Player> players) {
         return players.stream()
                 .filter(p -> p.getTimeTaken() != -1.0)
                 .toList();
    }
}
