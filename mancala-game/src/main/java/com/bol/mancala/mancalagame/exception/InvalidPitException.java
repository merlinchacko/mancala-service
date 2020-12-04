package com.bol.mancala.mancalagame.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidPitException extends RuntimeException {
    public InvalidPitException(String message) {
        super(message);
    }
}
