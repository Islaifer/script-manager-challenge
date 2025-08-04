package com.cooper_filme.auth_service.controller;

import com.cooper_filme.auth_service.model.LoginRequest;
import com.cooper_filme.auth_service.model.LoginResponse;
import com.cooper_filme.auth_service.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/")
public class SecurityController {
    private final SecurityService service;

    @Autowired
    public SecurityController(SecurityService service){
        this.service = service;
    }

    @PostMapping("login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request){
        LoginResponse response = service.authenticate(request);

        return ResponseEntity.ok(response);
    }
}
