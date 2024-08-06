package com.isitcake.game.entities.payloads;

import com.isitcake.game.enums.QuestionType;
import java.util.List;

public record SetupQuestionActionPayload(
        QuestionType questionType,
        String questionText,
        List<String> choices,
        Integer timer) { }
