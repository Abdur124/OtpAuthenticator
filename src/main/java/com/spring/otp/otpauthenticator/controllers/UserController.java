package com.spring.otp.otpauthenticator.controllers;

import com.spring.otp.otpauthenticator.dtos.OtpValidateDto;
import com.spring.otp.otpauthenticator.dtos.UserDetailsSetupDto;
import com.spring.otp.otpauthenticator.dtos.UserLoginDto;
import com.spring.otp.otpauthenticator.dtos.UserLoginResponseDto;
import com.spring.otp.otpauthenticator.models.OtpValidityStatus;
import com.spring.otp.otpauthenticator.models.SignupStatus;
import com.spring.otp.otpauthenticator.models.UserDetails;
import com.spring.otp.otpauthenticator.models.UserStatus;
import com.spring.otp.otpauthenticator.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

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
    public ResponseEntity<String> login(@RequestBody UserLoginDto userLoginDto) {
            String otpValue = userService.getOtpByAadhaarVerification(userLoginDto.getAadhaarId());
            return ResponseEntity.ok(otpValue);
    }

    @PostMapping("/verifyOtp")
    public ResponseEntity<OtpValidityStatus> verifyOtp(@RequestBody OtpValidateDto otpValidateDto) {
        OtpValidityStatus status = userService.verifyOtp(otpValidateDto.getAadhaar(), otpValidateDto.getOtp()) ? OtpValidityStatus.VALID : OtpValidityStatus.INVALID;
        return ResponseEntity.ok(status);
    }
}
