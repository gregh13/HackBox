package com.isitcake.game.dto.response.error;

import java.sql.Timestamp;

public class GameSessionNotFoundResponse extends ErrorResponseDto{
    public GameSessionNotFoundResponse(Integer status, String error, String message, Timestamp timestamp) {
        super(status, error, message, timestamp);
    }

    public GameSessionNotFoundResponse() {
    }

    public GameSessionNotFoundResponse(Integer status, String error, String message) {
        super(status, error, message);
    }
}
