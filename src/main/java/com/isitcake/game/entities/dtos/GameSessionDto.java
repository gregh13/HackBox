package com.isitcake.game.entities.dtos;

import com.isitcake.game.enums.StateType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameSessionDto {
    private String sessionId;
    private String gameState;
    private Boolean active;
    private Timestamp dateCreated;
    private List<PlayerDto> players;
}
