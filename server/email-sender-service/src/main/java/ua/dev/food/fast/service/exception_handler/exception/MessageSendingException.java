package ua.dev.food.fast.service.exception_handler.exception;

public class MessageSendingException extends RuntimeException {
    public MessageSendingException(String message) {
        super(message);
    }
}
