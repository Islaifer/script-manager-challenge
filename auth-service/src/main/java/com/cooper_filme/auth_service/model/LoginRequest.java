package com.cooper_filme.auth_service.model;

public record LoginRequest(String username, String email, String password) {
}
