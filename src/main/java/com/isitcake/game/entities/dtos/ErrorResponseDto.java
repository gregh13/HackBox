package com.isitcake.game.entities.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseDto {
    private String message;
    private String error;
    private Integer status;
    private Timestamp timestamp;

    public ErrorResponseDto(String message, int status, String error) {
        this.message = message;
        this.status = status;
        this.error = error;
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }
}

