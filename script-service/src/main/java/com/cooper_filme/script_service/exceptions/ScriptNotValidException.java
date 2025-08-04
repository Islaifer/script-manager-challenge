package com.cooper_filme.script_service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ScriptNotValidException extends RuntimeException {
    public ScriptNotValidException(String message) {
        super(message);
    }
}
