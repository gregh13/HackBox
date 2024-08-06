package com.isitcake.game.entities.payloads;

public record SubmitAnswerActionPayload(String playerName, String choice, Double timeTaken) { }
