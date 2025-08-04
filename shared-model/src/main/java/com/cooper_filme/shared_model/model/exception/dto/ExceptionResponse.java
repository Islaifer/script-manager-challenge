package com.cooper_filme.shared_model.model.exception.dto;

import java.time.LocalDateTime;

public record ExceptionResponse(LocalDateTime timestamp, String message, String details) {
}
