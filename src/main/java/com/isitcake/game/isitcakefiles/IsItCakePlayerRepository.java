package com.isitcake.game.isitcakefiles;

import com.isitcake.game.isitcakefiles.IsItCakePlayer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IsItCakePlayerRepository extends JpaRepository<IsItCakePlayer, Long> {
    List<IsItCakePlayer> findByGameSessionId(int gameSessionId);
    Optional<IsItCakePlayer> findByName(String name);

//    Optional<Player> findById(Long id);
}