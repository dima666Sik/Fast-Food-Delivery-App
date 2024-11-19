package ua.dev.food.fast.service.exception_handler;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ua.dev.food.fast.service.domain.dto.response.ErrorResponse;
import ua.dev.food.fast.service.exception_handler.exception.InternalServerException;
import ua.dev.food.fast.service.exception_handler.exception.ValidationException;
import ua.dev.food.fast.service.exception_handler.exception.ResourceNotFoundException;

@ControllerAdvice
@Log4j2
public class ExceptionsHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ErrorResponse.builder().message(ex.getMessage()).build());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleExceptionBadRequest(ValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse.builder().message(ex.getMessage()).build());
    }

    @ExceptionHandler({InternalServerException.class, Exception.class})
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("Failed to execute request, error: {}, StackTrace: {}", ex, ex.getStackTrace());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse.builder().message(ex.getMessage()).build());
    }
}
