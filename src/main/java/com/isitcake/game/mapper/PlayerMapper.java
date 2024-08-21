package com.isitcake.game.mapper;

import com.isitcake.game.dto.response.PlayerResponseDto;
import com.isitcake.game.entity.Player;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PlayerMapper {
    PlayerResponseDto entityToDto(Player player);
    List<PlayerResponseDto> entitiesToDtos(List<Player> players);
}
