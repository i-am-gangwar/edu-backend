package com.edubackend.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;


@Component
public class JWtUtil {

     @Value("${cred.jwt_SecretKey}")
     private String jwt_SecretKey ;


    // Generate a JWT token with an expiration time of 10 minutes
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 10 * 60 * 1000))  // 10 minutes expiration
                .signWith(Keys.hmacShaKeyFor(jwt_SecretKey.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }


    // Validate the JWT token and check the expiration
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(jwt_SecretKey.getBytes(StandardCharsets.UTF_8))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            System.out.println("Token is expired: " + e.getMessage());
        } catch (io.jsonwebtoken.SignatureException e) {
            System.out.println("Invalid token signature: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Token validation failed: " + e.getMessage());
        }
        return false;
    }




    // Extract the subject (email) from the token
    public String extractSubject(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(jwt_SecretKey.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}
