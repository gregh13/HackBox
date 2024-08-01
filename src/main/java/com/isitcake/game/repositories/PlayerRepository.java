package com.isitcake.game.repositories;

import com.isitcake.game.entities.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    List<Player> findByGameSessionId(int gameSessionId);
    Optional<Player> findByName(String name);

//    Optional<Player> findById(Long id);
}