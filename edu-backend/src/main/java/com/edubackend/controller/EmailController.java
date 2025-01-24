package com.edubackend.controller;

import com.edubackend.dto.UserDto;
import com.edubackend.repository.UserRepo;
import com.edubackend.service.EmailService;
import com.edubackend.service.OtpService;
import com.edubackend.utils.ApiResponse;
import com.edubackend.utils.JwtUtill;
import com.edubackend.utils.ResponseUtil;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class EmailController {

    @Autowired
    private EmailService emailService;
    @Autowired
    private OtpService otpService;
    @Autowired
    private UserRepo userRepo;
    private final JwtUtill jwtUtil = new JwtUtill();



    @PostMapping("/send-otp/{emailId}")
    public ResponseEntity<ApiResponse<Object>> sendOtpOnEmail(@PathVariable String emailId) throws MessagingException {
        Optional<UserDto> userDto = userRepo.findByContact(emailId);
        if(userDto.isEmpty())
            return ResponseUtil.success(emailService.sendOtpEmail(emailId), new ArrayList<>());
        else
            return ResponseUtil.success("User already exit in the system with this emailID", new ArrayList<>());
    }


    @PostMapping("/validateOtp/{emailId}/{otp}")
    public ResponseEntity<ApiResponse<Object>> validateOtpEmail(@PathVariable String emailId,@PathVariable String otp){
        return  ResponseUtil.success( otpService.verifyOtp(emailId,otp), new ArrayList<>());
    }


    @PostMapping("/forgot-password/{emailId}")
    public String sendForgotPassword(@PathVariable("emailId") String email) throws MessagingException {
        emailService.sendPasswordResetEmail(email);
        return "Password reset link has been sent to your email.";
    }




    @PostMapping("/reset-password/{token}")
    public String resetPassword(@PathVariable("token") String token, @RequestBody String newPassword) throws Exception {
        if (jwtUtil.validateToken(token)) {
            String email = jwtUtil.extractSubject(token);
            emailService.updatePassword(token,newPassword);
        }
        else {
            return "Invalid or expired token.";
        }
        return "no";
    }
}
