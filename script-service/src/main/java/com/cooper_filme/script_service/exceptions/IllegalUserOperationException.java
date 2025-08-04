package com.cooper_filme.script_service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class IllegalUserOperationException extends RuntimeException {
    public IllegalUserOperationException(String message) {
        super(message);
    }
}
