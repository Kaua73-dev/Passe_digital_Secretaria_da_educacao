package com.PasseDigital.system.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class TokenConfig {

    @Value("{jwt.secret}")
    private String secret;

    public String generateToken(User user){
        Algorithm algorithm = Algorithm.HMAC256(secret);
        try {
        String token = JWT.create()
                .withIssuer("carteirinha_project_secretaria")
                .withSubject(user.getUsername())
                .withIssuedAt(Instant.now())
                .withExpiresAt(generationExpiration())
                .sign(algorithm);
        return token;
        } catch (JWTCreationException e){
            throw new RuntimeException("It was not possible to generate the token: " + e);
        }
    }


    private Instant generationExpiration(){
        return LocalDateTime.now().plusMinutes(15).toInstant(ZoneOffset.of("-03:00"));
    }


    public String validateToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .build()
                    .verify(token)
                    .getSubject();

        } catch (JWTVerificationException e) {
            throw new RuntimeException("Error validate token: " + e);
        }
    }

}
