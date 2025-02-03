package com.edubackend.service;


import com.edubackend.model.Otp;
import com.edubackend.repository.OtpRepository;
import com.edubackend.utils.ApiResponse;
import com.edubackend.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class OtpService {

    @Autowired
    private OtpRepository otpRepository;
    private static final String DIGITS = "0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    public void saveOtp(String email, String otp, int validityMinutes) {
        Otp otpEntity = new Otp();
        otpEntity.setEmail(email);
        otpEntity.setOtp(otp);
        otpEntity.setExpirationTime(LocalDateTime.now().plusMinutes(validityMinutes));
        otpEntity.setVerified(false);
        Otp savedOtp = otpRepository.findByEmail(email);
        if (savedOtp != null){
            savedOtp.setOtp(otp);
            savedOtp.setExpirationTime(LocalDateTime.now().plusMinutes(validityMinutes));
            savedOtp.setVerified(false);
            otpRepository.save(savedOtp);
        }
        else
            otpRepository.save(otpEntity);



    }

    public ResponseEntity<ApiResponse<Object>> verifyOtp(String email, String inputOtp) {
       Otp otp = otpRepository.findByEmail(email);
        if (otp!=null) {
            if (otp.getOtp().equals(inputOtp)){
                if(otp.getExpirationTime().isAfter(LocalDateTime.now()) && !otp.isVerified()) {
                    otp.setVerified(true);
                    otpRepository.delete(otp);
                    return ResponseUtil.success( "Otp verified successfully.", new ArrayList<>());
                }
                else
                  return ResponseUtil.badRequest( "Otp expired please generate a new otp", new ArrayList<>());

            }
            else
                return ResponseUtil.badRequest( "Entered Otp not matching. Enter a valid otp.", new ArrayList<>());

        }
        return ResponseUtil.internalServerError( "Otp details not found in db enter the email on which otp was sent.", new ArrayList<>());

    }



    public String generateOtp(int length) {
        StringBuilder otp = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            otp.append(DIGITS.charAt(RANDOM.nextInt(DIGITS.length())));
        }
        return otp.toString();
    }

    public Otp findOtpByEmail(String email){
        return otpRepository.findByEmail(email);
    }

    public Boolean isAllowedToSendNewOtp(String email){
         Otp otp = findOtpByEmail(email);
         if(otp==null)
             return true;
        else if(otp.getExpirationTime().isAfter(LocalDateTime.now()))
             return false;
         else
             return true;
    }

}
