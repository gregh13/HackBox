package com.isitcake.game.dto.response.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseDto {
    private Integer status;
    private String error;
    private String message;
    private Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    public ErrorResponseDto(Integer status, String error, String message) {
        this.message = message;
        this.status = status;
        this.error = error;
    }
}

