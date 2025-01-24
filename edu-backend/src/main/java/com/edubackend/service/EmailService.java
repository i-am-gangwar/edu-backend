package com.edubackend.service;

import com.edubackend.dto.UserDto;
import com.edubackend.repository.UserRepo;
import com.edubackend.utils.JwtUtill;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

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
    private final JwtUtill jwtUtill = new JwtUtill();
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
    @Value(value = "${spring.mail.username}")
    private String senderEmail;



    public void sendEmail(String toAddress, String emailSubject, String emailBody) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toAddress);  // Receiver's email address
        message.setSubject(emailSubject);
        message.setText(emailBody);
        message.setFrom(senderEmail);  // Your email address (same as the SMTP configuration)
        javaMailSender.send(message);

    }



    public void sendPasswordResetEmail(String email) {
        String resetToken = jwtUtill.generateToken(email);
        String emailSubject = "Forget password reset";
        String resetLink = "http://localhost:8080/reset-password?token=" + resetToken;
        String userName = "There!";
        if(userRepo.findByContact(email).isPresent())
           userName = userRepo.findByContact(email).get().getUsername();
        String emailBody = "Hello,"+ userName +
        "\n\n" +
                "We received a request to reset your password. To proceed, please click the link below:\n\n" +
                "Reset Link: " + resetLink + "\n\n" +
                "This link will remain valid for the next 10 minutes. If you did not request a password reset, please ignore this email.\n\n" +
                "Best regards,\n" +
                "The GS by Vishnu Team";
        sendEmail(email,emailSubject, emailBody);
    }



    public String sendOtpEmail(String email){
        String otp = otpService.generateOtp(6);
        String emailSubject = "Otp Verification Email";
        String userName = "There!";
        if(userRepo.findByContact(email).isPresent())
            userName = userRepo.findByContact(email).get().getUsername();
        String emailBody = "Hello,"+ userName +
                "\n\n" +
                "Thank you for registering with us! To complete your registration, please use the OTP provided below:\n\n" +
                "OTP: " + otp + "\n\n" +
                "Please note that this OTP is valid for the next 5 minutes. If you did not request this, please ignore this email.\n\n" +
                "Best regards,\n" +
                "The GS by Vishnu Team";
        sendEmail(email,emailSubject, emailBody);
        otpService.saveOtp(email,otp,5);
        return "Otp email Sent successfully";
    }






    public String updatePassword(String email, String newPassword) throws Exception {
        Optional<UserDto> userDto = userRepo.findByContact(email);
       if(userDto.isPresent()){
           System.out.println("oldPWD:"+"1234567");
           System.out.println("oldHash:"+userDto.get().getPassword());
           System.out.println("match Result:"+ passwordEncoder.matches("1234567",userDto.get().getPassword()));
           String hash = passwordEncoder.encode(newPassword);
           userDto.get().setPassword(hash);
           userService.updateUser(userDto.get());
           System.out.println("newPWD:"+newPassword);
           System.out.println("newHash:"+hash);
           return "Password has been successfully reset.";
       }
       else
           return "Password can't be updated please try again!";
    }


}
