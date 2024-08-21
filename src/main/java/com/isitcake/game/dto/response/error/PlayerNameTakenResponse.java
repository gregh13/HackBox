package com.isitcake.game.dto.response.error;

import java.sql.Timestamp;

public class PlayerNameTakenResponse extends ErrorResponseDto{
    public PlayerNameTakenResponse(Integer status, String error, String message, Timestamp timestamp) {
        super(status, error, message, timestamp);
    }

    public PlayerNameTakenResponse() {
    }

    public PlayerNameTakenResponse(Integer status, String error, String message) {
        super(status, error, message);
    }
}
