package com.isitcake.game.entities.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerDto {
    private String name;
    private String sessionId;
    private Boolean sessionHost;
    private Integer score;
    private String choice;
    private Double timeTaken;
}
