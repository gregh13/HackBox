package com.isitcake.game.repository;

import com.isitcake.game.entity.GameSession;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface GameSessionRepository extends JpaRepository<GameSession, Long> {
    Optional<GameSession> findBySessionId(String sessionId);

    @Query("SELECT gs FROM GameSession gs WHERE gs.active = true")
    List<GameSession> findAllActiveSessions();

    @Query("SELECT gs FROM GameSession gs WHERE gs.dateCreated < :cutoffDate")
    List<GameSession> findAllCreatedBefore(Timestamp cutoffDate);

    @Modifying
    @Query("DELETE FROM GameSession gs WHERE gs.active = false")
    void deleteAllInactive();
}

