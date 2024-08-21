package com.isitcake.game.dto.payload;

import java.util.List;

public record SetupQuestionResponsePayload(
        String questionType,
        String questionText,
        List<String> choices,
        Integer timer,
        String questionId
) { }