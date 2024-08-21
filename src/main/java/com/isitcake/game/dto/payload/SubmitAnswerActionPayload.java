package com.isitcake.game.dto.payload;

public record SubmitAnswerActionPayload(
        String playerName,
        String choice,
        Double timeTaken,
        String questionId
) { }
