package ru.kharevich.userservice.util.exception;

public class UserModifyingException extends RuntimeException {
    public UserModifyingException(String message) {
        super(message);
    }
}
