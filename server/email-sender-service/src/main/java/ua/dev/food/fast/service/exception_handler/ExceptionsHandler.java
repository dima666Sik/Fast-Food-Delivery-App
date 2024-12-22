package ua.dev.food.fast.service.exception_handler;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ua.dev.food.fast.service.domain.dto.response.ErrorResponse;
import ua.dev.food.fast.service.exception_handler.exception.MessageSendingException;

@ControllerAdvice
@Log4j2
public class ExceptionsHandler {
    @ExceptionHandler(MessageSendingException.class)
    public ResponseEntity<ErrorResponse> handleExceptionBadRequest(MessageSendingException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse.builder().message(ex.getMessage()).build());
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("Failed to execute request, error: {}, StackTrace: {}", ex.getMessage(), ex.getStackTrace());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse.builder().message(ex.getMessage()).build());
    }
}
