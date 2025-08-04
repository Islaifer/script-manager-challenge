package com.cooper_filme.script_service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ScriptNotFoundException extends RuntimeException {
    public ScriptNotFoundException(String message) {
        super(message);
    }
}
