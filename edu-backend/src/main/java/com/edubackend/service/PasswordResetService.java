package com.edubackend.service;

import com.edubackend.dto.UserDto;
import com.edubackend.repository.UserRepo;
import com.edubackend.utils.JwtUtill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class PasswordResetService {

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    UserService userService;
    @Autowired
    UserRepo userRepo;
    private JwtUtill jwtUtill = new JwtUtill();
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);


    public void sendPasswordResetEmail(String email) {
        String resetToken = jwtUtill.generateToken(email);  // Step 1: Generate a reset token
        System.out.println(resetToken);    // Step 2: Generate the reset link (this can be a real link to a frontend page)
        String resetLink = "http://localhost:8080/reset-password?token=" + resetToken;
        sendTestEmail(email, resetLink);  // Step 3: Send the reset link to the user's email
    }




    public void sendTestEmail(String toAddress,String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toAddress);  // Receiver's email address
        message.setSubject("Forget password reset yad rakh lena is baar");
        message.setText("to reset the password click on link and update it: "+ resetLink);
        message.setFrom("rakeshgangwarjnv256@gmail.com");  // Your email address (same as the SMTP configuration)
        javaMailSender.send(message);
    }




    public String updatePassword(String email, String newPassword) throws Exception {
       Optional<UserDto> userDto = userRepo.findById(email);
       if(!userDto.isEmpty()){

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
