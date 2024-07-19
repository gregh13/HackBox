package com.isitcake.game.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.isitcake.game.dtos.EpisodeRequestDto;
import com.isitcake.game.dtos.EpisodeResponseDto;
import com.isitcake.game.entities.Episode;

@Mapper(componentModel = "spring", uses = { QuestionMapper.class })
public interface EpisodeMapper {

    EpisodeResponseDto entityToDto(Episode entity);

    List<EpisodeResponseDto> entitiesToDtos(List<Episode> entities);

    Episode requestDtoToEntity(EpisodeRequestDto dto);
}
