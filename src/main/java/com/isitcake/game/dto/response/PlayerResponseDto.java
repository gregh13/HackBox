package com.isitcake.game.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerResponseDto {
    private String name;
    private String sessionId;
    private Boolean sessionHost;
    private Integer score;
    private String choice;
    private Double timeTaken;
    private String questionId;
    private Boolean submitted;
}
