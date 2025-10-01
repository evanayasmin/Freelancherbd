package com.evanadev.freelancherbd.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component("aesUtil")
public class AESUtil {

    private final String SECRET_KEY;
    private static final String ALGORITHM = "AES";

    // Inject key from application.properties
    public AESUtil(@Value("${app.aes.secret-key}") String secretKey) {

        System.out.println(">>> Injected AES key = [" + secretKey + "]");
        System.out.println(">>> Before Trimming AES key length = " + secretKey.length());
        if (secretKey == null) {
            throw new IllegalArgumentException("AES key not found in application.properties!");
        }
        secretKey = secretKey.replaceAll("[\\s\\r\\n]", "");
        System.out.println(">>> AES key length = " + secretKey.length());

        // Ensure key length is valid (16, 24, or 32 bytes)
        if (!(secretKey.length() == 16 || secretKey.length() == 24 || secretKey.length() == 32)) {
            throw new IllegalArgumentException("Invalid AES key length. Must be 16, 24, or 32 characters.");
        }
        SECRET_KEY = secretKey.trim();
    }

    public String encryptId(Long id) {
        return encrypt(String.valueOf(id)); // instance method
    }

    // Encrypt text
    public String encrypt(String data) {
        try {
            SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encrypted = cipher.doFinal(data.getBytes());
            return Base64.getUrlEncoder().encodeToString(encrypted); // URL-safe
        } catch (Exception e) {
            throw new RuntimeException("Error while encrypting: " + e.getMessage(), e);
        }
    }

    // Decrypt text
    public String decrypt(String encryptedData) {
        System.out.println(">>> SECRET AES key = [" + SECRET_KEY + "]");
        try {
            SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decoded = Base64.getUrlDecoder().decode(encryptedData);
            return new String(cipher.doFinal(decoded));
        } catch (Exception e) {
            throw new RuntimeException("Error while decrypting: " + e.getMessage(), e);
        }
    }


    // Decrypt to Long ID
    public Long decryptId(String encryptedId) {
        return Long.parseLong(decrypt(encryptedId));
    }
}
