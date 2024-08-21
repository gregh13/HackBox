package com.isitcake.game.util;

import com.isitcake.game.dto.payload.SetupQuestionActionPayload;
import com.isitcake.game.dto.payload.SetupQuestionResponsePayload;

public class PayloadConverter {
    public static SetupQuestionResponsePayload toSetUpQuestionResponsePayload(SetupQuestionActionPayload setupQuestionActionPayload, String questionId){
        return new SetupQuestionResponsePayload(
                setupQuestionActionPayload.questionType().getValue(),
                setupQuestionActionPayload.questionText(),
                setupQuestionActionPayload.choices(),
                setupQuestionActionPayload.timer(),
                questionId
        );
    }
}
