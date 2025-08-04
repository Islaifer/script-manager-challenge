package com.cooper_filme.auth_service.exceptions.handler;

import com.cooper_filme.auth_service.exceptions.UnauthorizedException;
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

    @ExceptionHandler(UnauthorizedException.class)
    public final ResponseEntity<ExceptionResponse> handlerAuthorizationException(Exception ex, WebRequest request){
        return getResponse(ex, request, HttpStatus.UNAUTHORIZED);
    }

    private ResponseEntity<ExceptionResponse> getResponse(Exception ex, WebRequest request, HttpStatus status){
        ExceptionResponse response = new ExceptionResponse(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));

        return new ResponseEntity<>(response, status);
    }
}
