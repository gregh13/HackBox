package com.isitcake.game.mappers;

import com.isitcake.game.entities.GameSession;
import com.isitcake.game.entities.dtos.GameSessionDto;
import com.isitcake.game.enums.StateType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface GameSessionMapper {
    @Mapping(source = "gameState", target = "gameState", qualifiedByName = "gameStateToString")
    GameSessionDto entityToDto(GameSession gameSession);

    @Named("gameStateToString")
    static String gameStateToString(StateType gameState) {
        return gameState != null ? gameState.toString() : "unknown state";
    }
}
