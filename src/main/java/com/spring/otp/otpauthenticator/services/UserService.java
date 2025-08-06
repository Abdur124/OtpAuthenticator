package com.spring.otp.otpauthenticator.services;

import com.spring.otp.otpauthenticator.exceptions.InvalidAadhaarIdException;
import com.spring.otp.otpauthenticator.models.*;
import com.spring.otp.otpauthenticator.repositories.OtpDeliveryRepository;
import com.spring.otp.otpauthenticator.repositories.UserDetailsRepository;
import com.spring.otp.otpauthenticator.utils.OtpEncryptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final long CACHE_TTL_VALUE_IN_MINUTES = 5;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private OtpService otpService;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    private RedisService redisService;

    public UserDetails saveUserDetails(UserDetails userDetails) {
        return userDetailsRepository.save(userDetails);
    }

    public String getOtpByAadhaarVerification(String aadhar) {

        UserDetails userDetails = userDetailsRepository.findUserDetailsByAadhaarId(aadhar);

        if(userDetails == null) {
            throw new InvalidAadhaarIdException("Given Aadhaar Id is invalid");
        }

        OtpDetails otpDetails = otpService.validateAndGenerateOtp(userDetails);
        String decryptedOtp = OtpEncryptionUtil.decryptOtp(otpDetails.getOtpValue());

        redisService.cacheOtp(otpDetails.getUser().getAadhaarId(), decryptedOtp,
                CACHE_TTL_VALUE_IN_MINUTES);

        String otpMessage = String.format("aadhaar:%s|otp:%s|mobile:%s|email:%s",
                otpDetails.getUser().getAadhaarId(),
                decryptedOtp,
                userDetails.getMobileNumber(),
                userDetails.getEmail()
        );

        kafkaProducerService.sendOtpMessage(otpMessage);

        OtpDelivery otpDelivery = otpService.updateOtpDeliveryDetails(otpDetails);

        return decryptedOtp;
    }

    public boolean verifyOtp(String aadhaar, String otp) {
        return otpService.validateOtp(aadhaar, otp);
    }

}
