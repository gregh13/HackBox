package com.isitcake.game.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.isitcake.game.dtos.QuestionResponseDto;
import com.isitcake.game.entities.Question;

@Mapper(componentModel = "spring")
public interface QuestionMapper {

    QuestionResponseDto entityToDto(Question entity);

    List<QuestionResponseDto> entitiesToDtos(List<Question> entities);
}
