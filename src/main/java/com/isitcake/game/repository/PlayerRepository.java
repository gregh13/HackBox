package com.isitcake.game.repository;

import com.isitcake.game.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    Optional<Player> findByNameAndSessionId(String name, String sessionId);

    List<Player> findAllBySessionId(String sessionId);
}
