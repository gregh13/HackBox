package com.isitcake.game.dto.response.error;

import java.sql.Timestamp;

public class PlayerNotFoundResponse extends ErrorResponseDto {
    public PlayerNotFoundResponse(Integer status, String error, String message, Timestamp timestamp) {
        super(status, error, message, timestamp);
    }

    public PlayerNotFoundResponse() {
    }

    public PlayerNotFoundResponse(Integer status, String error, String message) {
        super(status, error, message);
    }
}
