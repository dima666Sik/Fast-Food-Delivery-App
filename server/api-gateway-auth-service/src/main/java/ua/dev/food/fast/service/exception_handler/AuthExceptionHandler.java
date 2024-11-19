package ua.dev.food.fast.service.exception_handler;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;
import ua.dev.food.fast.service.domain.dto.response.ErrorResponse;
import ua.dev.food.fast.service.exception_handler.exception.AuthorizationTokenException;
import ua.dev.food.fast.service.exception_handler.exception.IncorrectUserDataException;
import ua.dev.food.fast.service.exception_handler.exception.UserAlreadyExistsException;
import ua.dev.food.fast.service.util.ConstantMessageExceptions;

@ControllerAdvice
@Log4j2
public class AuthExceptionHandler {
    @ExceptionHandler(UserAlreadyExistsException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse.builder().message(e.getMessage()).build()));
    }

    @ExceptionHandler(IncorrectUserDataException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleUserAlreadyExistsException(IncorrectUserDataException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        if (ConstantMessageExceptions.INCORRECT_EMAIL.equals(e.getMessage())) {
            status = HttpStatus.NOT_FOUND;
        }

        return Mono.just(ResponseEntity.status(status).body(ErrorResponse.builder().message(e.getMessage()).build()));
    }

    @ExceptionHandler(AuthorizationTokenException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleAuthorizationTokenException(AuthorizationTokenException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        if (ConstantMessageExceptions.INVALID_TOKEN.equals(e.getMessage())) {
            status = HttpStatus.UNAUTHORIZED;
        } else if (ConstantMessageExceptions.ACCESS_TOKEN_NOT_FOUND.equals(e.getMessage())) {
            status = HttpStatus.NOT_FOUND;
        } else if (ConstantMessageExceptions.TOKENS_WERE_REFRESHED.equals(e.getMessage())) {
            status = HttpStatus.CREATED;
        }
        return Mono.just(ResponseEntity.status(status).body(ErrorResponse.builder().message(e.getMessage()).build()));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ErrorResponse>> handleGenericException(Exception e) {
        log.error("Failed to execute request, error: {}, StackTrace: {}", e.getMessage(), e.getStackTrace());
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse.builder().message(ConstantMessageExceptions.INTERNAL_SERVER_ERROR).build()));
    }
}
