package com.isitcake.game.mappers;

import com.isitcake.game.entities.GameSession;
import com.isitcake.game.entities.dtos.GameSessionDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GameSessionMapper {
    GameSessionDto entityToDto(GameSession gameSession);
}
