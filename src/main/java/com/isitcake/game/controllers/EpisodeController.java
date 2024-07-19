package com.isitcake.game.controllers;

import com.isitcake.game.dtos.EpisodeResponseDto;
import com.isitcake.game.services.EpisodeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/episodes")
public class EpisodeController {
    private final EpisodeService episodeService;

    public EpisodeController(EpisodeService episodeService) {
        this.episodeService = episodeService;
    }

    @GetMapping
    public ResponseEntity<List<EpisodeResponseDto>> getAllEpisodes() {
        List<EpisodeResponseDto> episodes = episodeService.getAllEpisodes();
        return ResponseEntity.ok(episodes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EpisodeResponseDto> getEpisodeById(@PathVariable Long id) {
        EpisodeResponseDto episode = episodeService.getEpisodeById(id);
        return episode != null ? ResponseEntity.ok(episode) : ResponseEntity.notFound().build();
    }

    @GetMapping("/season/{season}")
    public ResponseEntity<List<EpisodeResponseDto>> getEpisodesBySeason(@PathVariable int season) {
        List<EpisodeResponseDto> episodes = episodeService.getEpisodesBySeason(season);
        return ResponseEntity.ok(episodes);
    }

    @GetMapping("/season/{season}/episode/{episodeNumber}")
    public ResponseEntity<EpisodeResponseDto> getEpisodeBySeasonAndEpisodeNumber(
            @PathVariable int season, @PathVariable int episodeNumber) {
        EpisodeResponseDto episode = episodeService.getEpisodeBySeasonAndEpisodeNumber(season, episodeNumber);
        return episode != null ? ResponseEntity.ok(episode) : ResponseEntity.notFound().build();
    }
}
