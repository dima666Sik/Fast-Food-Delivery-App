package ua.dev.food.fast.service.exception_handler.exception;

public class AuthorizationTokenException extends RuntimeException {
    public AuthorizationTokenException(String message) {
        super(message);
    }
}
