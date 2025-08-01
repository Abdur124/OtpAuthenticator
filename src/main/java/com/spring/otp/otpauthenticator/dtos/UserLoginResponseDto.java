package com.spring.otp.otpauthenticator.dtos;

public class UserLoginResponseDto {

    private String otpValue;

    public String getOtpValue() {
        return otpValue;
    }

    public void setOtpValue(String otpValue) {
        this.otpValue = otpValue;
    }
}
