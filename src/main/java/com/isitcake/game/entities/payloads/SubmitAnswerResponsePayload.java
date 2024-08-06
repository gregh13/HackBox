package com.isitcake.game.entities.payloads;

public record SubmitAnswerResponsePayload(String playerName, String choice, Double timeTaken) { }

