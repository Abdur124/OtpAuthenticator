package com.spring.otp.otpauthenticator.repositories;

import com.spring.otp.otpauthenticator.models.OtpDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpDetailsRepository extends JpaRepository<OtpDetails, Integer> {

}
