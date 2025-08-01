package com.spring.otp.otpauthenticator.repositories;

import com.spring.otp.otpauthenticator.models.OtpDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OtpDetailsRepository extends JpaRepository<OtpDetails, Integer> {

    Optional<OtpDetails> findTopByUser_AadhaarIdOrderByCreationTimeDesc(String userAadhaarId);
}
