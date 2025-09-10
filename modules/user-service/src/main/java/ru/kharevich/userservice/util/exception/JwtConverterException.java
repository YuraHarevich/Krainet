package ru.kharevich.userservice.util.exception;

public class JwtConverterException extends RuntimeException {
    public JwtConverterException(String message) {
        super(message);
    }
}
