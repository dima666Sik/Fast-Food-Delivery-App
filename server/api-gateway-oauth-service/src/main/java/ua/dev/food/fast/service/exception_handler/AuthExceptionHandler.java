package ua.dev.food.fast.service.exception_handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;
import ua.dev.food.fast.service.exception_handler.exception.AuthorizationTokenException;
import ua.dev.food.fast.service.exception_handler.exception.IncorrectUserDataException;
import ua.dev.food.fast.service.exception_handler.exception.UserAlreadyExistsException;
import ua.dev.food.fast.service.util.ConstantMessageExceptions;

@ControllerAdvice
public class AuthExceptionHandler {
    @ExceptionHandler(UserAlreadyExistsException.class)
    public Mono<ResponseEntity<String>> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()));
    }

    @ExceptionHandler(IncorrectUserDataException.class)
    public Mono<ResponseEntity<String>> handleUserAlreadyExistsException(IncorrectUserDataException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        if (ConstantMessageExceptions.INCORRECT_EMAIL.equals(e.getMessage())) {
            status = HttpStatus.NOT_FOUND;
        }

        return Mono.just(ResponseEntity.status(status).body(e.getMessage()));
    }

    @ExceptionHandler(AuthorizationTokenException.class)
    public Mono<ResponseEntity<String>> handleAuthorizationTokenException(AuthorizationTokenException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        if (ConstantMessageExceptions.INVALID_TOKEN.equals(e.getMessage())) {
            status = HttpStatus.UNAUTHORIZED;
        } else if (ConstantMessageExceptions.ACCESS_TOKEN_NOT_FOUND.equals(e.getMessage())) {
            status = HttpStatus.NOT_FOUND;
        } else if (ConstantMessageExceptions.TOKENS_WERE_REFRESHED.equals(e.getMessage())) {
            status = HttpStatus.CREATED;
        }
        return Mono.just(ResponseEntity.status(status).body(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<String>> handleGenericException(Exception e) {
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ConstantMessageExceptions.INTERNAL_SERVER_ERROR));
    }
}
