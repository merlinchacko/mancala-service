package com.bol.mancala.mancalagame.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PitNotFoundException extends RuntimeException {

    public PitNotFoundException(String message) {
        super(message);
    }
}
