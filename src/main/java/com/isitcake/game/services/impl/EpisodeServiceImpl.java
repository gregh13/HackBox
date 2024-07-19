package com.isitcake.game.services.impl;

import com.isitcake.game.dtos.EpisodeResponseDto;
import com.isitcake.game.mappers.EpisodeMapper;
import com.isitcake.game.repositories.EpisodeRepository;
import com.isitcake.game.services.EpisodeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EpisodeServiceImpl implements EpisodeService {
    private final EpisodeRepository episodeRepository;
    private final EpisodeMapper episodeMapper;

    public EpisodeServiceImpl(EpisodeRepository episodeRepository, EpisodeMapper episodeMapper) {
        this.episodeRepository = episodeRepository;
        this.episodeMapper = episodeMapper;
    }

    @Override
    public List<EpisodeResponseDto> getAllEpisodes() {
        return episodeMapper.entitiesToDtos(episodeRepository.findAll());
    }

    @Override
    public EpisodeResponseDto getEpisodeById(Long id) {
        return episodeRepository.findById(id)
                .map(episodeMapper::entityToDto)
                .orElse(null);
    }

    @Override
    public List<EpisodeResponseDto> getEpisodesBySeason(int season) {
        return episodeMapper.entitiesToDtos(episodeRepository.findBySeason(season));
    }

    @Override
    public EpisodeResponseDto getEpisodeBySeasonAndEpisodeNumber(int season, int episodeNumber) {
        return episodeRepository.findBySeasonAndEpisodeNumber(season, episodeNumber)
                .map(episodeMapper::entityToDto)
                .orElse(null);
    }
}
