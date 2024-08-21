package com.isitcake.game.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameSessionResponseDto {
    private String sessionId;
    private String gameState;
    private Boolean active;
    private Timestamp dateCreated;
    private List<PlayerResponseDto> players;
}
