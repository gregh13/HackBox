package com.isitcake.game.entities.payloads;

import com.isitcake.game.enums.QuestionType;

import java.util.List;

public record SetupQuestionResponsePayload(
        QuestionType questionType,
        String questionText,
        List<String> choices,
        Integer timer) { }