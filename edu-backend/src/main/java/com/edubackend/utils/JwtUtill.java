package com.edubackend.utils;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;

import java.util.Base64;
import java.util.Date;

public class JwtUtill {

   private String secretKey = Base64.getEncoder().encodeToString("my-very-secure-keynnnnnnnnnnnnnjjjjjjjsjjsjsjsjjsjsjjsjsjjsjsjjsjsjjjsjjsjsjsjsjjsjsjs".getBytes());
    // Use a more secure secret key in production

    //This class will handle the creation and validation of the JWT token, including setting the expiration time.
    // Generate a JWT token
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 20 * 60 * 1000))  // 10 minutes expiration
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                .compact();
    }

    // Validate the token and check the expiration
    public boolean validateToken(String token) {
        try {

            Jwts.parser()
                    .setSigningKey(secretKey.getBytes())
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;  // Token is invalid or expired
        }
    }

    // Extract the subject (email) from the token
    public String extractSubject(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}
