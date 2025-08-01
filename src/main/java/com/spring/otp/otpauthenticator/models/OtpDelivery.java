package com.spring.otp.otpauthenticator.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

@Entity
public class OtpDelivery extends BaseModel{

    @ManyToOne
    private OtpDetails otpDetails;

    private boolean isDelivered;
    private String deliveryTarget; // actual email/mobile
    private DeliveryType deliveryType;
    private DeliveryStatus deliveryStatus;
    private LocalDateTime deliveryTimestamp;
    @Column(nullable = false)
    private boolean isDeliveryRecorded;

    public OtpDetails getOtpDetails() {
        return otpDetails;
    }

    public void setOtpDetails(OtpDetails otpDetails) {
        this.otpDetails = otpDetails;
    }

    public boolean isDelivered() {
        return isDelivered;
    }

    public void setDelivered(boolean delivered) {
        isDelivered = delivered;
    }

    public String getDeliveryTarget() {
        return deliveryTarget;
    }

    public void setDeliveryTarget(String deliveryTarget) {
        this.deliveryTarget = deliveryTarget;
    }

    public DeliveryType getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(DeliveryType deliveryType) {
        this.deliveryType = deliveryType;
    }

    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public LocalDateTime getDeliveryTimestamp() {
        return deliveryTimestamp;
    }

    public void setDeliveryTimestamp(LocalDateTime deliveryTimestamp) {
        this.deliveryTimestamp = deliveryTimestamp;
    }

    public boolean isDeliveryRecorded() {
        return isDeliveryRecorded;
    }

    public void setDeliveryRecorded(boolean deliveryRecorded) {
        isDeliveryRecorded = deliveryRecorded;
    }
}
