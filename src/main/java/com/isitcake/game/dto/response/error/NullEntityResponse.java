package com.isitcake.game.dto.response.error;

import java.sql.Timestamp;

public class NullEntityResponse extends ErrorResponseDto {
    public NullEntityResponse(Integer status, String error, String message, Timestamp timestamp) {
        super(status, error, message, timestamp);
    }

    public NullEntityResponse() {
    }

    public NullEntityResponse(Integer status, String error, String message) {
        super(status, error, message);
    }
}
