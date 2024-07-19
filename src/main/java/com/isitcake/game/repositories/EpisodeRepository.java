package com.isitcake.game.repositories;

import com.isitcake.game.entities.Episode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EpisodeRepository extends JpaRepository<Episode, Long> {
    List<Episode> findBySeason(int season);
    Optional<Episode> findBySeasonAndEpisodeNumber(int season, int episodeNumber);
}
