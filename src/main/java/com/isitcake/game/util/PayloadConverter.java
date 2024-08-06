package com.isitcake.game.util;

import com.isitcake.game.entities.payloads.SetupQuestionActionPayload;
import com.isitcake.game.entities.payloads.SetupQuestionResponsePayload;
import com.isitcake.game.entities.payloads.SubmitAnswerActionPayload;
import com.isitcake.game.entities.payloads.SubmitAnswerResponsePayload;

public class PayloadConverter {
    public static SetupQuestionResponsePayload toResponsePayload(SetupQuestionActionPayload setupQuestionActionPayload){
        return new SetupQuestionResponsePayload(
                setupQuestionActionPayload.questionType(),
                setupQuestionActionPayload.questionText(),
                setupQuestionActionPayload.choices(),
                setupQuestionActionPayload.timer()
        );
    }

    public static SubmitAnswerResponsePayload toResponsePayload(SubmitAnswerActionPayload submitAnswerActionPayload){
        return new SubmitAnswerResponsePayload(
                submitAnswerActionPayload.playerName(),
                submitAnswerActionPayload.choice(),
                submitAnswerActionPayload.timeTaken()
        );
    }
}
