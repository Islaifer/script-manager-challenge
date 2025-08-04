package com.cooper_filme.auth_service.service;

import com.cooper_filme.auth_service.exceptions.UnauthorizedException;
import com.cooper_filme.auth_service.model.LoginRequest;
import com.cooper_filme.auth_service.model.LoginResponse;
import com.cooper_filme.shared_model.model.entity.Role;
import com.cooper_filme.shared_model.model.entity.User;
import com.cooper_filme.shared_model.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SecurityService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Autowired
    public SecurityService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponse authenticate(LoginRequest request){
        String unauthorizedError = "Not Found a user with this combination of user/email and password";
        User user = userRepository.findByName(request.username()).orElse(null);
        if(user == null){
            user = userRepository.findByEmail(request.email()).orElseThrow(() -> new UnauthorizedException(unauthorizedError));
        }

        if(passwordEncoder.matches(request.password(), user.getPassword())){
            List<String> roles = user.getRoles().stream().map(Role::getName).toList();
            return new LoginResponse(generateToken(user.getId(), roles));
        }else{
            throw new UnauthorizedException(unauthorizedError);
        }

    }

    private String generateToken(Long id, List<String> roles){
        long expiration = 14_400_000;

        try {
            JWSSigner signer = new MACSigner(jwtSecret);
            JWTClaimsSet claims = new JWTClaimsSet.Builder()
                    .subject(id.toString())
                    .claim("roles", roles)
                    .expirationTime(new Date(System.currentTimeMillis() + expiration))
                    .build();

            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claims);
            signedJWT.sign(signer);

            return signedJWT.serialize();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
