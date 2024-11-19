package ua.dev.food.fast.service.exception_handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;
import ua.dev.food.fast.service.domain.dto.response.ErrorResponse;
import ua.dev.food.fast.service.exception_handler.exception.AuthorizationTokenException;
import ua.dev.food.fast.service.exception_handler.exception.IncorrectUserDataException;
import ua.dev.food.fast.service.util.ConstantMessageExceptions;

import java.util.Map;
import java.util.Optional;

@Component
@Order(-1)
@Log4j2
public class GlobalAuthWebExceptionHandler implements WebExceptionHandler {
    @Override
    public @NotNull Mono<Void> handle(@NotNull ServerWebExchange exchange, @NotNull Throwable ex) {
        Map<String, HttpStatus> exceptionStatusMap = Map.of(
            ConstantMessageExceptions.ACCESS_TOKEN_HAS_REVOKED, HttpStatus.UNAUTHORIZED,
            ConstantMessageExceptions.INVALID_TOKEN, HttpStatus.UNAUTHORIZED,
            ConstantMessageExceptions.ACCESS_TOKEN_HAS_EXPIRED, HttpStatus.UNAUTHORIZED,
            ConstantMessageExceptions.ACCESS_REFRESH_TOKENS_HAVE_EXPIRED, HttpStatus.UNAUTHORIZED,
            ConstantMessageExceptions.REFRESH_TOKEN_NOT_FOUND, HttpStatus.NOT_FOUND,
            ConstantMessageExceptions.ACCESS_TOKEN_NOT_FOUND, HttpStatus.NOT_FOUND,
            ConstantMessageExceptions.AUTHORIZATION_HEADER_IS_EMPTY, HttpStatus.NOT_FOUND,
            ConstantMessageExceptions.USER_NOT_FOUND, HttpStatus.NOT_FOUND,
            ConstantMessageExceptions.UNSUCCESSFUL_LOGOUT, HttpStatus.FORBIDDEN
        );
        Optional<HttpStatus> message = Optional.ofNullable(exceptionStatusMap.get(ex.getMessage()));

        log.error("Failed to execute request, message error: {}, Cause: {}, StackTrace: {}", message, ex, ex.getStackTrace());

        if (message.isPresent()) {
            return setReactiveJsonStatus(exchange, ex.getMessage(), message.get());
        }

        return setReactiveJsonStatus(exchange, ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    public Mono<Void> setReactiveJsonStatus(ServerWebExchange exchange, String message, HttpStatus statusCode) {
        ErrorResponse errorResponse = ErrorResponse.builder().message(message).build();
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(statusCode);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        try {
            ObjectMapper mapper = new ObjectMapper();

            byte[] bytes = mapper.writeValueAsBytes(errorResponse);
            DataBuffer buffer = response.bufferFactory().wrap(bytes);

            return response.writeWith(Mono.just(buffer));
        } catch (Exception e) {
            log.error("Failed to set response status, error: {}", e.getMessage(), e);
            return Mono.error(e);
        }
    }
}