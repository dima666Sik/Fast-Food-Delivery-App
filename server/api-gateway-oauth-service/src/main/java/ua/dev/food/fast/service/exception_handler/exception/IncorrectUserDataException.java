package ua.dev.food.fast.service.exception_handler.exception;

public class IncorrectUserDataException extends RuntimeException {
    public IncorrectUserDataException(String message) {
        super(message);
    }
}
