package com.edubackend.controller;

import com.edubackend.service.PasswordResetService;
import com.edubackend.utils.JwtUtill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class PasswordResetController {

    @Autowired
    private PasswordResetService passwordResetService;


    private JwtUtill jwtUtil = new JwtUtill();  // Initialize JWT Utility


    // Endpoint for forgot password (sends reset link to user's email)
    @PostMapping("/forgot-password/{emailId}")
    public String handleForgotPassword(@PathVariable("emailId") String email) {
        passwordResetService.sendPasswordResetEmail(email);
        return "Password reset link has been sent to your email.";
    }

    // Endpoint for resetting the password
    @PostMapping("/reset-password/{token}")
    public String resetPassword(@PathVariable("token") String token, @RequestBody String newPassword) throws Exception {
        passwordResetService.updatePassword(token,newPassword);
        if (jwtUtil.validateToken(token)) {
            String email = jwtUtil.extractSubject(token);

        }
        else {
            return "Invalid or expired token.";
        }
        return "no";
    }
}
