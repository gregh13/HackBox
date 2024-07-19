package com.isitcake.game.dtos;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class QuestionResponseDto {
    private Long id;
    private int questionNumber;
    private int numberChoices;
    private int correctChoice;
    private long episodeTimestamp;
    private int durationSeconds;
}
