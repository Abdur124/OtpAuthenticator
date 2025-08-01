package com.spring.otp.otpauthenticator.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class OtpDetails extends BaseModel{

    private String otpValue;
    private boolean isValidated;
    private boolean isExpired;
    private int attemptCount;

    private LocalDateTime expiryTime;
    private LocalDateTime validatedAt;

    @ManyToOne
    private UserDetails user;

    @OneToMany(mappedBy = "otpDetails", cascade = CascadeType.ALL)
    List<OtpDelivery> deliveries;

    public String getOtpValue() {
        return otpValue;
    }

    public void setOtpValue(String otpValue) {
        this.otpValue = otpValue;
    }

    public boolean isValidated() {
        return isValidated;
    }

    public void setValidated(boolean validated) {
        isValidated = validated;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }

    public int getAttemptCount() {
        return attemptCount;
    }

    public void setAttemptCount(int attemptCount) {
        this.attemptCount = attemptCount;
    }

    public LocalDateTime getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(LocalDateTime expiryTime) {
        this.expiryTime = expiryTime;
    }

    public LocalDateTime getValidatedAt() {
        return validatedAt;
    }

    public void setValidatedAt(LocalDateTime validatedAt) {
        this.validatedAt = validatedAt;
    }

    public UserDetails getUser() {
        return user;
    }

    public void setUser(UserDetails user) {
        this.user = user;
    }

    public List<OtpDelivery> getDeliveries() {
        return deliveries;
    }

    public void setDeliveries(List<OtpDelivery> deliveries) {
        this.deliveries = deliveries;
    }
}
