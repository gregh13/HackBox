package com.isitcake.game.mapper;

import com.isitcake.game.entity.GameSession;
import com.isitcake.game.dto.response.GameSessionResponseDto;
import com.isitcake.game.type.StateType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface GameSessionMapper {
    @Mapping(source = "gameState", target = "gameState", qualifiedByName = "gameStateToString")
    GameSessionResponseDto entityToDto(GameSession gameSession);

    @Named("gameStateToString")
    static String gameStateToString(StateType gameState) {
        return gameState != null ? gameState.toString() : "unknown state";
    }
}
