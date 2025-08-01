package com.spring.otp.otpauthenticator.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.otp.otpauthenticator.exceptions.UserDetailsNotFoundException;
import com.spring.otp.otpauthenticator.models.*;
import com.spring.otp.otpauthenticator.repositories.OtpDeliveryRepository;
import com.spring.otp.otpauthenticator.repositories.OtpDetailsRepository;
import com.spring.otp.otpauthenticator.repositories.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class OtpService {

    @Autowired
    private OtpDetailsRepository otpDetailsRepository;

    @Autowired
    private OtpDeliveryRepository otpDeliveryRepository;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private ObjectMapper objectMapper;

    private static final int OTP_VALIDITY_MINUTES = 5;

    public String generateOtp() {
        Random random = new Random();
        int randomNumber = random.nextInt(900000) + 100000;
        return String.valueOf(randomNumber);
    }

    public OtpDetails validateAndGenerateOtp(UserDetails userDetails) {

        String otpValue = generateOtp();
        OtpDetails otpDetails = new OtpDetails();
        otpDetails.setOtpValue(otpValue);
        otpDetails.setCreationTime(LocalDateTime.now());
        otpDetails.setExpiryTime(LocalDateTime.now().plusMinutes(OTP_VALIDITY_MINUTES));
        otpDetails.setValidated(false);
        otpDetails.setExpired(false);
        otpDetails.setAttemptCount(0);
        otpDetails.setUser(userDetails);
        return otpDetailsRepository.save(otpDetails);

    }

    public OtpDelivery updateOtpDeliveryDetails(OtpDetails otpDetails) {

        OtpDelivery otpDelivery = new OtpDelivery();
        otpDelivery.setOtpDetails(otpDetails);
        otpDelivery.setDelivered(true);
        otpDelivery.setDeliveryStatus(DeliveryStatus.SUCCESS);
        otpDelivery.setDeliveryType(DeliveryType.MOBILE);
        otpDelivery.setDeliveryTimestamp(LocalDateTime.now());
        otpDelivery.setDeliveryTarget(otpDetails.getUser().getMobileNumber());

        return otpDeliveryRepository.save(otpDelivery);
    }

    public boolean validateOtp(String aadhaar, String otpSupplied) {

        String cacheOtp = stringRedisTemplate.opsForValue().get(aadhaar);
        System.out.println("OTP cached in Redis: " +cacheOtp);

        if(cacheOtp != null && cacheOtp.equals(otpSupplied)) {
            return true;
        }

        OtpDetails latestOtp = otpDetailsRepository.findTopByUser_AadhaarIdOrderByCreationTimeDesc(aadhaar)
                .orElseThrow(() -> new RuntimeException("No OTP Details Found"));

        if(latestOtp == null || latestOtp.getExpiryTime().isBefore(LocalDateTime.now())) {
            return false;
        }

        return latestOtp.getOtpValue().equals(otpSupplied);
    }

    @KafkaListener(topics = "otp-ack", groupId = "acknowledgment")
    public void updateOtpDelivery(String message) {
        try {
            OtpDelivery incomingAck = new ObjectMapper().readValue(message, OtpDelivery.class);
            System.out.println("Received Ack: " + incomingAck);

            Optional<OtpDelivery> existing = otpDeliveryRepository.findById(incomingAck.getOtpDetails().getId());

            if (existing.isPresent()) {
                OtpDelivery existingRecord = existing.get();

                if (!existingRecord.isDeliveryRecorded()) {
                    // Update only once
                    existingRecord.setDeliveryStatus(incomingAck.getDeliveryStatus());
                    existingRecord.setDelivered(true);
                    existingRecord.setDeliveryType(incomingAck.getDeliveryType());
                    existingRecord.setDeliveryRecorded(true); // block further updates
                    otpDeliveryRepository.save(existingRecord);
                    System.out.println("DB updated for OTP ID: " + incomingAck.getOtpDetails().getId());
                } else {
                    System.out.println("Duplicate ack received, DB already updated.");
                }
            } else {
                // First-time save (if using UPSERT style)
                incomingAck.setDeliveryRecorded(true);
                otpDeliveryRepository.save(incomingAck);
                System.out.println("First-time delivery ack saved.");
            }

        } catch (JsonProcessingException e) {
            System.err.println("Invalid JSON in Kafka ack: " + e.getMessage());
        }
    }
}
