package com.isitcake.game.isitcakefiles;

import com.isitcake.game.isitcakefiles.IsItCakeGameSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IsItCakeGameSessionRepository extends JpaRepository<IsItCakeGameSession, Long> {
    Optional<IsItCakeGameSession> findBySessionId(String sessionId);

    @Query("SELECT gs FROM GameSession gs WHERE gs.isActive = true")
    List<IsItCakeGameSession> findAllActiveSessions();
}

