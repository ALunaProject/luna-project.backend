package com.luna.lunaproject.application.services;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ReservedUsernameException extends RuntimeException {

    public ReservedUsernameException(String message) {
        super(message);
    }
}
