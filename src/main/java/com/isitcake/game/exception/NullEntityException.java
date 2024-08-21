package com.isitcake.game.exception;

public class NullEntityException extends RuntimeException {
    public NullEntityException(String message) {
        super(message);
    }

    public NullEntityException(String message, Throwable cause) {
        super(message, cause);
    }

    public NullEntityException(Throwable cause) {
        super(cause);
    }
}
