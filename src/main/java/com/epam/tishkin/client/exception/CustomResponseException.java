package com.epam.tishkin.client.exception;

public class CustomResponseException extends RuntimeException {

    public CustomResponseException(String message) {
        super(message);
    }
}
