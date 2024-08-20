package com.isitcake.game.exception;

public class GameSessionNotFoundException extends RuntimeException {
    public GameSessionNotFoundException(String message) {
        super(message);
    }

    public GameSessionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public GameSessionNotFoundException(Throwable cause) {
        super(cause);
    }
}
