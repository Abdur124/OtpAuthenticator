package com.spring.otp.otpauthenticator.repositories;

import com.spring.otp.otpauthenticator.models.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, Integer> {

}
