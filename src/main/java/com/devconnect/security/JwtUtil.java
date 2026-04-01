package com.devconnect.security;


import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expiration}")
    private long expiration;

//    Create a Jwt token for the user
    public String generateToken(String email){
        Key key = Keys.hmacShaKeyFor(secret.getBytes());

        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration ))
                .signWith(key)
                .compact();
    }

//    Reads the email form a token
    public String getEmailFromToken(String token){
        Key key = Keys.hmacShaKeyFor(secret.getBytes());

        return Jwts.parser()
                .verifyWith((SecretKey)key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();


//        Checking if token is val

    }
    public boolean validateToken(String token ){
        try {
            Key key = Keys.hmacShaKeyFor(secret.getBytes());
            Jwts.parser()
                    .verifyWith((javax.crypto.SecretKey)key)
                    .build()
                    .parseSignedClaims(token);
            return true ;


        } catch (JwtException e){
            return false ;
        }

    }
}
