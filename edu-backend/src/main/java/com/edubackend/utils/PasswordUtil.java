package com.edubackend.utils;

import org.apache.commons.codec.digest.HmacUtils;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordUtil {
    private static final String SECRET_KEY = "123456789abcdefghijkl"; // Same as Node.js
    public static String encryptPassword(String password, String salt) {
        return HmacUtils.hmacSha256Hex(SECRET_KEY, password + salt);
    }

//    public static String generateSalt() {
//        byte[] saltBytes = new byte[16];
//        new SecureRandom().nextBytes(saltBytes);
//        System.out.println( Base64.getEncoder().encodeToString(saltBytes));
//        return Base64.getEncoder().encodeToString(saltBytes);
//    }
}
