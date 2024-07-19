package com.isitcake.game.services.impl;

import com.isitcake.game.dtos.EpisodeResponseDto;
import com.isitcake.game.dtos.QuestionResponseDto;
import com.isitcake.game.entities.Episode;
import com.isitcake.game.entities.Question;
import com.isitcake.game.repositories.EpisodeRepository;
import com.isitcake.game.services.EpisodeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EpisodeServiceImpl implements EpisodeService {
    private final EpisodeRepository episodeRepository;

    public EpisodeServiceImpl(EpisodeRepository episodeRepository) {
        this.episodeRepository = episodeRepository;
    }

    @Override
    public List<EpisodeResponseDto> getAllEpisodes() {
        return episodeRepository.findAll().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public EpisodeResponseDto getEpisodeById(Long id) {
        return episodeRepository.findById(id)
                .map(this::mapToResponseDto)
                .orElse(null);
    }

    @Override
    public List<EpisodeResponseDto> getEpisodesBySeason(int season) {
        return episodeRepository.findBySeason(season).stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public EpisodeResponseDto getEpisodeBySeasonAndEpisodeNumber(int season, int episodeNumber) {
        return episodeRepository.findBySeasonAndEpisodeNumber(season, episodeNumber)
                .map(this::mapToResponseDto)
                .orElse(null);
    }

    private EpisodeResponseDto mapToResponseDto(Episode episode) {
        EpisodeResponseDto responseDto = new EpisodeResponseDto();
        responseDto.setId(episode.getId());
        responseDto.setSeason(episode.getSeason());
        responseDto.setEpisodeNumber(episode.getEpisodeNumber());
        responseDto.setDescription(episode.getDescription());
        responseDto.setQuestions(episode.getQuestions().stream()
                .map(this::mapToQuestionResponseDto)
                .collect(Collectors.toList()));
        return responseDto;
    }

    private QuestionResponseDto mapToQuestionResponseDto(Question question) {
        QuestionResponseDto questionDto = new QuestionResponseDto();
        questionDto.setId(question.getId());
        questionDto.setQuestionNumber(question.getQuestionNumber());
        questionDto.setNumberChoices(question.getNumberChoices());
        questionDto.setCorrectChoice(question.getCorrectChoice());
        questionDto.setEpisodeTimestamp(question.getEpisodeTimestamp());
        questionDto.setDurationSeconds(question.getDurationSeconds());
        return questionDto;
    }
}
