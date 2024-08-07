package com.isitcake.game.mappers;

import com.isitcake.game.entities.GameSession;
import com.isitcake.game.entities.dtos.GameSessionDto;

public interface GameSessionMapper {
    GameSessionDto entityToDto(GameSession gameSession);
}
