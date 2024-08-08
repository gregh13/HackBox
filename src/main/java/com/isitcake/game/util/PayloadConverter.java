package com.isitcake.game.util;

import com.isitcake.game.entities.payloads.SetupQuestionActionPayload;
import com.isitcake.game.entities.payloads.SetupQuestionResponsePayload;

public class PayloadConverter {
    public static SetupQuestionResponsePayload toResponsePayload(SetupQuestionActionPayload setupQuestionActionPayload){
        return new SetupQuestionResponsePayload(
                setupQuestionActionPayload.questionType().getValue(),
                setupQuestionActionPayload.questionText(),
                setupQuestionActionPayload.choices(),
                setupQuestionActionPayload.timer()
        );
    }
}
