package com.cooper_filme.script_service.security.service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;

@Service
public class JWTTokenService {
    @Value("${jwt.secret}")
    private String jwtSecret;
    private JWSVerifier verifier;

    public JWTTokenService(){
    }

    @PostConstruct
    public void init() {
        try {
            this.verifier = new MACVerifier(jwtSecret.getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public JWTClaimsSet validateToken(String token) throws ParseException, JOSEException{
        SignedJWT signedJWT = SignedJWT.parse(token);

        if(!signedJWT.verify(verifier))
            throw new JOSEException("Invalid token");

        JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

        Date expDate = claims.getExpirationTime();
        if(expDate == null || expDate.before(new Date()))
            throw  new JOSEException("Expired token");

        return claims;
    }
}
