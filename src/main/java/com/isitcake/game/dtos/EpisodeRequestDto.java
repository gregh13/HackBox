package com.isitcake.game.dtos;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class EpisodeRequestDto {
    private int season;
    private int episodeNumber;
}
