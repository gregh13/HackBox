package com.isitcake.game.controller.advice;

import com.isitcake.game.dto.response.GameSessionNotFoundResponse;
import com.isitcake.game.dto.response.PlayerNameTakenResponse;
import com.isitcake.game.exception.GameSessionNotFoundException;
import com.isitcake.game.exception.PlayerNameTakenException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<PlayerNameTakenResponse> handlePlayerNameTakenException(PlayerNameTakenException exc) {
        return null;
    }

    @ExceptionHandler
    public ResponseEntity<GameSessionNotFoundResponse> handleGameSessionNotFoundException(GameSessionNotFoundException exc) {
        return null;
    }
}
