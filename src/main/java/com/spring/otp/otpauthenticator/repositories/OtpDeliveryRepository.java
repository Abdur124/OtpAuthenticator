package com.spring.otp.otpauthenticator.repositories;

import com.spring.otp.otpauthenticator.models.OtpDelivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpDeliveryRepository extends JpaRepository<OtpDelivery, Integer> {

}
