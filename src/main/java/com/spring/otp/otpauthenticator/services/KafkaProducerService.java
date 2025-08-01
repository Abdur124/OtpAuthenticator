package com.spring.otp.otpauthenticator.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private static final String TOPIC = "send-otp";

    public void sendOtpMessage(String message) {
        kafkaTemplate.send(TOPIC, message);
    }
}
