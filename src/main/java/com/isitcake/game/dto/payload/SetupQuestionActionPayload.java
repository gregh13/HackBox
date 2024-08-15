package com.isitcake.game.dto.payload;

import com.isitcake.game.type.QuestionType;
import java.util.List;

public record SetupQuestionActionPayload(
        QuestionType questionType,
        String questionText,
        List<String> choices,
        Integer timer) { }
