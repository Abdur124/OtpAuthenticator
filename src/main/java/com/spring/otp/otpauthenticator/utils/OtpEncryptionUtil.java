package com.spring.otp.otpauthenticator.utils;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Properties;

@Component
public class OtpEncryptionUtil {

    @Value("${otp.encrypt.algorithm}")
    private String algorithm;

    @Value("${otp.encrypt.secret_key}")
    private String secretKey;

    private static String ALGORITHM;
    private static String SECRET_KEY;

    @PostConstruct
    public void init() {
        ALGORITHM = algorithm;
        SECRET_KEY = secretKey;
    }


    public static String encryptOtp(String otp) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encryptedOtp = cipher.doFinal(otp.getBytes());
            return Base64.getEncoder().encodeToString(encryptedOtp);
        } catch(Exception e) {
            throw new RuntimeException("Error while encrypting OTP ", e);
        }
    }

    public static String decryptOtp(String encryptedOtp) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decodedOtp = Base64.getDecoder().decode(encryptedOtp);
            return new String(cipher.doFinal(decodedOtp));
        } catch(Exception e) {
            throw new RuntimeException("Error while decrypting OTP ", e);
        }
    }

}
