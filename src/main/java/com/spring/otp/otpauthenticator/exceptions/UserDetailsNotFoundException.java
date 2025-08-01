package com.spring.otp.otpauthenticator.exceptions;

public class UserDetailsNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UserDetailsNotFoundException() {

    }

    public UserDetailsNotFoundException(String message) {
        super(message);
    }
}
