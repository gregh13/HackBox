package com.isitcake.game.services;

import com.isitcake.game.dtos.EpisodeResponseDto;

import java.util.List;

public interface EpisodeService {
    List<EpisodeResponseDto> getAllEpisodes();
    EpisodeResponseDto getEpisodeById(Long id);
    List<EpisodeResponseDto> getEpisodesBySeason(int season);
    EpisodeResponseDto getEpisodeBySeasonAndEpisodeNumber(int season, int episodeNumber);
}
