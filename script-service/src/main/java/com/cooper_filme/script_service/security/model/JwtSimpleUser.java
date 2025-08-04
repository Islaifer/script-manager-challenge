package com.cooper_filme.script_service.security.model;

import java.util.List;

public record JwtSimpleUser(Long userId, List<String> roles) {
}
