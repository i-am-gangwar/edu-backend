package com.edubackend.service;

import com.edubackend.dto.UserDto;
import com.edubackend.repository.UserRepo;
import com.edubackend.utils.JwtUtill;
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
        System.out.println("senderEmail:"+senderEmail);
        message.setFrom(senderEmail);  // Your email address (same as the SMTP configuration)
        javaMailSender.send(message);

    }



    public void sendPasswordResetEmail(String email) {
        String resetToken = jwtUtill.generateToken(email);
        String emailSubject = "Forget password reset";
        String resetLink = "http://localhost:8080/reset-password?token=" + resetToken;
        String emailBody = "Hi there\nTo reset the password click on below link, link is valid for 10 minutes only\n"+ resetLink;
        sendEmail(email,emailSubject, emailBody);
    }



    public String sendOtpEmail(String email){
        String otp = otpService.generateOtp(7);
        String emailSubject = "Otp Verification Email";
        String emailBody = "Hi there\n To Register into the portal please under the below otp, Otp is valid for 10 minutes only\n"+ otp;
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
