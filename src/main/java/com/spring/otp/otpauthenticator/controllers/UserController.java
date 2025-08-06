package com.spring.otp.otpauthenticator.controllers;

import com.spring.otp.otpauthenticator.dtos.OtpValidateDto;
import com.spring.otp.otpauthenticator.dtos.UserDetailsSetupDto;
import com.spring.otp.otpauthenticator.dtos.UserLoginDto;
import com.spring.otp.otpauthenticator.dtos.UserLoginResponseDto;
import com.spring.otp.otpauthenticator.models.OtpValidityStatus;
import com.spring.otp.otpauthenticator.models.SignupStatus;
import com.spring.otp.otpauthenticator.models.UserDetails;
import com.spring.otp.otpauthenticator.models.UserStatus;
import com.spring.otp.otpauthenticator.services.RateLimitService;
import com.spring.otp.otpauthenticator.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.catalina.util.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RateLimitService rateLimitService;

    @PostMapping("/signUp")
    public ResponseEntity<SignupStatus> signUp(@RequestBody UserDetailsSetupDto userDetailsSetupDto) {
        UserDetails userDetails = new UserDetails();
        userDetails.setUsername(userDetailsSetupDto.getUsername());
        userDetails.setAadhaarId(userDetailsSetupDto.getAadhaarId());
        userDetails.setEmail(userDetailsSetupDto.getEmail());
        userDetails.setMobileNumber(userDetailsSetupDto.getMobileNumber());
        userDetails.setStatus(UserStatus.ACTIVE);
        userDetails.setVerified(true);
        userDetails = userService.saveUserDetails(userDetails);

        if(userDetails!=null) {
            return ResponseEntity.ok(SignupStatus.SUCCESS);
        }

        return ResponseEntity.ok(SignupStatus.FAILURE);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginDto userLoginDto, HttpServletRequest httpServletRequest) {
        String aadhaar = userLoginDto.getAadhaarId();
        String ip = httpServletRequest.getRemoteAddr();

        boolean allowedUser = rateLimitService.isAllowed("user: " + aadhaar, 5, Duration.ofMinutes(10));
        boolean allowedIp = rateLimitService.isAllowed("ip: " + ip, 20, Duration.ofHours(1));

        if(!allowedUser) {
            return ResponseEntity.status(429).body("Too many OTP requests for this user. Try later.");
        }

        if(!allowedIp) {
            return ResponseEntity.status(429).body("Too many OTP requests from this IP. Try later.");
        }

        String otpValue = userService.getOtpByAadhaarVerification(userLoginDto.getAadhaarId());
        return ResponseEntity.ok(otpValue);
    }

    @PostMapping("/verifyOtp")
    public ResponseEntity<OtpValidityStatus> verifyOtp(@RequestBody OtpValidateDto otpValidateDto) {
        OtpValidityStatus status = userService.verifyOtp(otpValidateDto.getAadhaar(), otpValidateDto.getOtp()) ? OtpValidityStatus.VALID : OtpValidityStatus.INVALID;
        return ResponseEntity.ok(status);
    }
}
