package com.isitcake.game.exception;

public class PlayerNameTakenException extends RuntimeException {
    public PlayerNameTakenException(String message) {
        super(message);
    }

    public PlayerNameTakenException(String message, Throwable cause) {
        super(message, cause);
    }

    public PlayerNameTakenException(Throwable cause) {
        super(cause);
    }
}
