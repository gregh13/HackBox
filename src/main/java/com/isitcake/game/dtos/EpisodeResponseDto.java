package com.isitcake.game.dtos;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class EpisodeResponseDto {
    private Long id;
    private int season;
    private int episodeNumber;
    private String description;
    private List<QuestionResponseDto> questions;
}

