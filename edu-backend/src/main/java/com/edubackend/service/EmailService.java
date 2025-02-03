package com.edubackend.service;

import com.edubackend.dto.UserDto;
import com.edubackend.repository.UserRepo;
import com.edubackend.utils.JWtUtil;
import com.edubackend.utils.PasswordUtil;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.MessagingException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.time.ZoneId;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private OtpService otpService;
    @Autowired
    private JWtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
    @Value(value = "${spring.mail.username}")
    private String senderEmail;
    @Autowired
    private PasswordUtil passwordUtil;


//
//    public void sendEmail(String toAddress, String emailSubject, String emailBody) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(toAddress);  // Receiver's email address
//        message.setSubject(emailSubject);
//        message.setText(emailBody);
//        message.setFrom(senderEmail);  // Your email address (same as the SMTP configuration)
//        javaMailSender.send(message);
//
//    }



    public void sendHtmlEmail(String to, String subject, String htmlContent) throws MessagingException, jakarta.mail.MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);
        messageHelper.setText(htmlContent, true); // set to true to indicate HTML content
        messageHelper.setFrom(senderEmail);
        javaMailSender.send(mimeMessage);
    }


    public String sendOtpEmail(String email) throws Exception {
        if(otpService.isAllowedToSendNewOtp(email)) {
            String otp = otpService.generateOtp(6);
            String emailSubject = "Otp Verification Email";
            String userName = "There!";
            if (userRepo.findByContact(email).isPresent())
                userName = userRepo.findByContact(email).get().getUsername();
            String htmlContent = "<html><body style='font-family: Arial, sans-serif; background-color: #f4f7f6; color: #333;'>" +
                    "<div style='width: 100%; background-color: #4CAF50; color: white; text-align: center; padding: 20px; font-size: 24px;'>" +
                    "Welcome to GS by Vishnu Sir App</div>" +
                    "<div style='max-width: 600px; margin: 20px auto; padding: 30px; background-color: #ffffff; border: 2px solid #ddd; border-radius: 8px;'>" +
                    "<h2 style='color: #333;'>Hello, " + userName + "!</h2>" +
                    "<p style='font-size: 16px; line-height: 1.6;'>Thank you for registering with us! To complete your registration, please use the OTP provided below:</p>" +
                    "<div style='background-color: #f1f1f1; padding: 10px; font-size: 18px; font-weight: bold; text-align: center; margin: 20px 0; border-radius: 6px;'>" +
                    "<span style='color: #007bff;'>OTP: " + otp + "</span></div>" +
                    "<p style='font-size: 16px; line-height: 1.6;'>This OTP is valid for the next 10 minutes. If you did not request this, please ignore this email.</p>" +
                    "<p style='font-size: 16px; color: #777;'>Best regards,<br>The GS by Vishnu Team</p>" +
                    "</div>" +
                    "<div style='text-align: center; font-size: 14px; color: #aaa; padding: 10px; background-color: #f4f7f6;'>" +
                    "If you have any questions, feel free to contact us at " + senderEmail + "</div>" +
                    "</body></html>";
            sendHtmlEmail(email, emailSubject, htmlContent);
            otpService.saveOtp(email, otp, 10);
            return "Otp email Sent successfully";
        }
        else {
            String istTime = otpService.findOtpByEmail(email)
                    .getExpirationTime()
                    .atZone(ZoneId.of("UTC")) // Treat LocalDateTime as UTC
                    .withZoneSameInstant(ZoneId.of("Asia/Kolkata")) // Convert to IST
                    .format(DateTimeFormatter.ofPattern("hh:mm:ss a")); // Format output

            String date = otpService.findOtpByEmail(email)
                    .getExpirationTime()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")
                    );
            return "Otp email already has been sent use that otp or Please try after " + istTime + " "
                    + date;
        }
    }


    public String sendPasswordResetEmail(String email) throws Exception {
        String resetToken = jwtUtil.generateToken(email);
        String emailSubject = "Forget password reset";
        String resetLink = "https://gsbyvishnusir.com/reset-password?token=" + resetToken;
        String userName = "There!";
        if(userRepo.findByContact(email).isPresent())
            userName = userRepo.findByContact(email).get().getUsername();
        String htmlContent = "<html><body style='font-family: Arial, sans-serif; background-color: #f4f7f6; color: #333;'>" +
                "<div style='width: 100%; background-color: #4CAF50; color: white; text-align: center; padding: 20px; font-size: 24px;'>" +
                "Welcome to GS by Vishnu Sir App</div>" +
                "<div style='max-width: 600px; margin: 20px auto; padding: 30px; background-color: #ffffff; border: 2px solid #ddd; border-radius: 8px;'>" +
                "<h2 style='color: #333;'>Hello, " + userName + "!</h2>" +
                "<p style='font-size: 16px; line-height: 1.6;'>Thank you for actively being with us! To reset your password, please use the link provided below:</p>" +
                "<div style='background-color: #f1f1f1; padding: 10px; font-size: 18px; font-weight: bold; text-align: center; margin: 20px 0; border-radius: 6px;'>" +
                "<span style='color: #007bff;'>" + resetLink + "</span></div>" +
                "<p style='font-size: 16px; line-height: 1.6;'>This link is valid for the next 10 minutes. If you did not request this, please ignore this email.</p>" +
                "<p style='font-size: 16px; color: #777;'>Best regards,<br>The GS by Vishnu Team</p>" +
                "</div>" +
                "<div style='text-align: center; font-size: 14px; color: #aaa; padding: 10px; background-color: #f4f7f6;'>" +
                "If you have any questions, feel free to contact us at " + senderEmail + "</div>" +
                "</body></html>";
        sendHtmlEmail(email,emailSubject,htmlContent);
        return "Password reset link has been sent to your email.";
    }


    public String updatePassword(String email, String newPassword) throws Exception {
        Optional<UserDto> userDto = userRepo.findByContact(email);
       if(userDto.isPresent()){
           String newHash = passwordUtil.customEncryptPassword(newPassword);
           userDto.get().setPassword(newHash);
           userRepo.save(userDto.get());
           return "Password has been updated successfully";
       }
       else
           throw new Exception("data not found");
    }


}
