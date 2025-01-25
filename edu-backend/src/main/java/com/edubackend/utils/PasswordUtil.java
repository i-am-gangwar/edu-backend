package com.edubackend.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PasswordUtil {

    @Value(value = "${cred.salt_Key}")
    private  String saltKey;



    // XOR each character of the password with the saltKey
    public  String xorEncrypt(String password) throws NullPointerException{
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < password.length(); i++) {
            char passChar = password.charAt(i);
            char saltChar = saltKey.charAt(i % saltKey.length());
            result.append((char) (passChar ^ saltChar)); // XOR operation
        }
        return result.toString();
    }


    // Encrypt password with custom transformations
    public  String customEncryptPassword(String password) {
        String xorEncrypted = xorEncrypt(password);
        // Step 3: Apply additional transformation (mixing)
        StringBuilder transformed = new StringBuilder();
        for (int i = 0; i < xorEncrypted.length(); i++) {
            int charCode = xorEncrypted.charAt(i);
            transformed.append((char) ((charCode + i) % 256)); // Add position index
        }
        // Step 4: Final hashing-like step (reversible, for learning)
        StringBuilder finalHash = new StringBuilder();
        for (int i = 0; i < transformed.length(); i++) {
            finalHash.append(Integer.toHexString(transformed.charAt(i))); // Convert to hexadecimal
        }
        return  finalHash.toString();
    }



    // Verify the password by comparing hashes
    public  boolean verifyPassword(String inputPassword, String originalHash) {
        // Recompute the hash for the input password
        String xorEncrypted = xorEncrypt(inputPassword);
        // Apply the transformation
        StringBuilder transformed = new StringBuilder();
        for (int i = 0; i < xorEncrypted.length(); i++) {
            int charCode = xorEncrypted.charAt(i);
            transformed.append((char) ((charCode + i) % 256));
        }
        // Convert to final hash in hexadecimal form
        StringBuilder computedHash = new StringBuilder();
        for (int i = 0; i < transformed.length(); i++) {
            computedHash.append(Integer.toHexString(transformed.charAt(i)));
        }
        // Compare the hashes
        return computedHash.toString().equals(originalHash);
    }




}
