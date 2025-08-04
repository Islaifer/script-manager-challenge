package com.cooper_filme.script_service.exceptions.handler;

import com.cooper_filme.script_service.exceptions.IllegalUserOperationException;
import com.cooper_filme.script_service.exceptions.ScriptNotFoundException;
import com.cooper_filme.script_service.exceptions.ScriptNotValidException;
import com.cooper_filme.shared_model.model.exception.dto.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@RestController
@ControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler{

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionResponse> handlerDefaultExceptions(Exception ex, WebRequest request){
        return getResponse(ex, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ScriptNotFoundException.class)
    public final ResponseEntity<ExceptionResponse> handlerScriptNotFoundException(Exception ex, WebRequest request){
        return getResponse(ex, request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ScriptNotValidException.class)
    public final ResponseEntity<ExceptionResponse> handlerScriptNotValidException(Exception ex, WebRequest request){
        return getResponse(ex, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    public final ResponseEntity<ExceptionResponse> handlerUnsupportedOperationException(Exception ex, WebRequest request){
        return getResponse(ex, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalUserOperationException.class)
    public final ResponseEntity<ExceptionResponse> handlerIllegalUserOperationException(Exception ex, WebRequest request){
        return getResponse(ex, request, HttpStatus.UNAUTHORIZED);
    }

    private ResponseEntity<ExceptionResponse> getResponse(Exception ex, WebRequest request, HttpStatus status){
        ExceptionResponse response = new ExceptionResponse(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));

        return new ResponseEntity<>(response, status);
    }
}