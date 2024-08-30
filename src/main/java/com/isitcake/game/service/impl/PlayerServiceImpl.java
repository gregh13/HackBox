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
        List<Player> players = getPlayers(gameSession.getSessionId());
        for (Player player : players ) {
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
    public Player getPlayer(String playerName, String sessionId) {
        Optional<Player> playerOpt = playerRepository.findByNameAndSessionId(playerName, sessionId);

        if (playerOpt.isEmpty()) {
            String message = "Player '" + playerName + "' could not be found";
            throw new PlayerNotFoundException(message);
        }

        return playerOpt.get();
    }

    @Override
    public List<Player> getPlayers(String sessionId) {
        return playerRepository.findAllBySessionId(sessionId);
    }

    @Override
    public PlayerResponseDto updatePlayerAnswer(String sessionId, String playerName, String choice, Double timeTaken, String questionId) {
        Player player = getPlayer(playerName, sessionId);
        player.setChoice(choice);
        player.setTimeTaken(timeTaken);
        player.setQuestionId(questionId);
        player.setSubmitted(true);
        return getPlayerDto(playerRepository.saveAndFlush(player));
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

    @Override
    public List<Player> resetPlayerAnswers(List<Player> players, String questionId) {
        for (Player player : players) {
            player.setChoice("");
            player.setTimeTaken(-1.0);
            player.setQuestionId(questionId);
            player.setSubmitted(false);
        }
        return playerRepository.saveAllAndFlush(players);
    }

    @Transactional
    @Override
    public List<Player> transferHost(String sessionId, String playerName) {
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
        return playerRepository.saveAllAndFlush(players);
    }

    @Override
    public List<Player> getActivePlayersAndDeleteInactive(List<Player> players, String questionId) {
        List<Player> remainingPlayers = new ArrayList<Player>();

        for (Player p : players) {
            if (p.getQuestionId().equals(questionId) || p.getQuestionId().isEmpty() || p.getSessionHost()) {
                remainingPlayers.add(p);
            } else {
                playerRepository.delete(p);
            }
        }

        return remainingPlayers;
    }

    @Override
    public void removePlayer(String playerName, String sessionId) {
        Player playerToRemove = getPlayer(playerName, sessionId);
        playerRepository.delete(playerToRemove);
    }
}
