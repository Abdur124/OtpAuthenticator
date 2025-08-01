package com.spring.otp.otpauthenticator.exceptions;

public class InvalidAadhaarIdException extends RuntimeException {

    InvalidAadhaarIdException() {

    }

    public InvalidAadhaarIdException(String message) {
        super(message);
    }
}
