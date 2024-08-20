package com.isitcake.game.controller.advice;

import com.isitcake.game.dto.response.error.GameSessionNotFoundResponse;
import com.isitcake.game.dto.response.error.NullEntityResponse;
import com.isitcake.game.dto.response.error.PlayerNameTakenResponse;
import com.isitcake.game.dto.response.error.PlayerNotFoundResponse;
import com.isitcake.game.exception.GameSessionNotFoundException;
import com.isitcake.game.exception.NullEntityException;
import com.isitcake.game.exception.PlayerNameTakenException;
import com.isitcake.game.exception.PlayerNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<NullEntityResponse> handleNullEntityException(NullEntityException exc) {
        // Create response variables
        Integer status = HttpStatus.NOT_FOUND.value();
        String error = "Null Entity Error";
        String message = exc.getMessage();

        // Create response body
        NullEntityResponse nullEntityResponse = new NullEntityResponse(status, error, message);

        // Create and return response entity
        return new ResponseEntity<>(nullEntityResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<PlayerNameTakenResponse> handlePlayerNameTakenException(PlayerNameTakenException exc) {
        // Create response variables
        Integer status = HttpStatus.BAD_REQUEST.value();
        String error = "Player Name Taken";
        String message = exc.getMessage();


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
        String message = exc.getMessage();

        // Create response body
        GameSessionNotFoundResponse gameSessionNotFoundResponse = new GameSessionNotFoundResponse(status, error, message);

        // Create and return response entity
        return new ResponseEntity<>(gameSessionNotFoundResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<PlayerNotFoundResponse> handlePlayerNotFoundException(PlayerNotFoundException exc) {
        // Create response variables
        Integer status = HttpStatus.NOT_FOUND.value();
        String error = "Player Not Found";
        String message = exc.getMessage();

        // Create response body
        PlayerNotFoundResponse playerNotFoundResponse = new PlayerNotFoundResponse(status, error, message);

        // Create and return response entity
        return new ResponseEntity<>(playerNotFoundResponse, HttpStatus.NOT_FOUND);
    }

}
