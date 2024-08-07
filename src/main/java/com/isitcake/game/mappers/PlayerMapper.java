package com.isitcake.game.mappers;

import com.isitcake.game.entities.Player;
import com.isitcake.game.entities.dtos.PlayerDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PlayerMapper {
    PlayerDto entityToDto(Player player);
    List<PlayerDto> entitiesToDtos(List<Player> players);
}
