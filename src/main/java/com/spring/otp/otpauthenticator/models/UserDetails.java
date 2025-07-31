package com.spring.otp.otpauthenticator.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

import java.util.List;

@Entity
public class UserDetails extends BaseModel{

    @Column(unique = true, nullable = false)
    private String aadhaarId;

    @Column(nullable = false)
    private String username;

    @Email
    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String mobileNumber;

    private boolean isVerified;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @OneToMany(mappedBy = "user")
    private List<OtpDetails> otps;

    public String getAadhaarId() {
        return aadhaarId;
    }

    public void setAadhaarId(String aadhaarId) {
        this.aadhaarId = aadhaarId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public List<OtpDetails> getOtps() {
        return otps;
    }

    public void setOtps(List<OtpDetails> otps) {
        this.otps = otps;
    }
}
