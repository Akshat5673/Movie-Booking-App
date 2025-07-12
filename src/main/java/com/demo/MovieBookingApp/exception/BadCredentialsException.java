package com.demo.MovieBookingApp.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BadCredentialsException extends RuntimeException {
    private final String message;
    private final boolean success;

    public BadCredentialsException(String message) {
        super(message);
        this.message = message;
        this.success = false;
    }
}
