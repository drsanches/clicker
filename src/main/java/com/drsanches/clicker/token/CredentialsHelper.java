package com.drsanches.clicker.token;

import com.drsanches.clicker.exception.AuthException;
import com.drsanches.clicker.exception.ServerError;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class CredentialsHelper {

    public void checkPassword(String rawPassword, String encodedPassword, String salt) {
        if (!encodePassword(rawPassword, salt).equals(encodedPassword)) {
            throw new AuthException("Wrong password");
        }
    }

    public String encodePassword(String password, String salt) {
        return sha256(password + salt);
    }

    private String sha256(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder(2 * encodedHash.length);
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new ServerError("SHA256 error", e);
        }
    }
}
