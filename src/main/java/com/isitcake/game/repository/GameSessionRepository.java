package com.isitcake.game.repository;

import com.isitcake.game.entity.GameSession;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface GameSessionRepository extends JpaRepository<GameSession, Long> {
    Optional<GameSession> findBySessionId(String sessionId);

    @Query("SELECT gs FROM GameSession gs JOIN FETCH gs.players WHERE gs.sessionId = :sessionId")
    Optional<GameSession> findBySessionIdFetchPlayers(String sessionId);

    @Query("SELECT gs FROM GameSession gs WHERE gs.active = true")
    List<GameSession> findAllActiveSessions();

    @Query("SELECT gs FROM GameSession gs WHERE gs.dateCreated < :cutoffDate")
    List<GameSession> findAllCreatedBefore(Timestamp cutoffDate);

    @Modifying
    @Query("DELETE FROM GameSession gs WHERE gs.active = false")
    void deleteAllInactive();

    @Modifying
    @Query("DELETE FROM GameSession gs WHERE gs.dateCreated < :cutoffDate")
    void deleteAllOldSessions(Timestamp cutoffDate);
}

