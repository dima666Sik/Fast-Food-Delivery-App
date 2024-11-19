package ua.dev.food.fast.service.exception_handler.exception;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
