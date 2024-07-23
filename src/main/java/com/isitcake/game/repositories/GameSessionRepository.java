package com.isitcake.game.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.isitcake.game.entities.GameSession;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GameSessionRepository extends JpaRepository<GameSession, Long> {
    Optional<GameSession> findBySessionId(String sessionId);

    @Query("SELECT gs FROM GameSession gs WHERE gs.isActive = true")
    List<GameSession> findAllActiveSessions();
}

