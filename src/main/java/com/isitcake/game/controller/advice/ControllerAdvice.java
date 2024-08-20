package com.isitcake.game.controller.advice;

import com.isitcake.game.dto.response.error.GameSessionNotFoundResponse;
import com.isitcake.game.dto.response.error.PlayerNameTakenResponse;
import com.isitcake.game.exception.GameSessionNotFoundException;
import com.isitcake.game.exception.PlayerNameTakenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<PlayerNameTakenResponse> handlePlayerNameTakenException(PlayerNameTakenException exc) {
        // Create response variables
        Integer status = HttpStatus.BAD_REQUEST.value();
        String error = "Player Name Taken";
        String message = "The player name already exists in the game session and cannot be used again. Please try a new name to join the session.";

        // Create response body
        PlayerNameTakenResponse playerNameTakenResponse = new PlayerNameTakenResponse(status, error, message);

        // Create and return response entity
        return new ResponseEntity<>(playerNameTakenResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<GameSessionNotFoundResponse> handleGameSessionNotFoundException(GameSessionNotFoundException exc) {
        // Create response variables
        Integer status = HttpStatus.NOT_FOUND.value();
        String error = "Game Session Not Found";
        String message = "No game session was found with the given session id. Please try again after ensuring there are no typos or capitalization issues with the session id.";

        // Create response body
        GameSessionNotFoundResponse gameSessionNotFoundResponse = new GameSessionNotFoundResponse(status, error, message);

        // Create and return response entity
        return new ResponseEntity<>(gameSessionNotFoundResponse, HttpStatus.NOT_FOUND);
    }
}
