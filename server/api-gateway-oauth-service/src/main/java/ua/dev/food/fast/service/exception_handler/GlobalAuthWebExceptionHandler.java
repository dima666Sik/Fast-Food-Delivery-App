package ua.dev.food.fast.service.exception_handler;

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
import ua.dev.food.fast.service.exception_handler.exception.AuthorizationTokenException;
import ua.dev.food.fast.service.exception_handler.exception.IncorrectUserDataException;
import ua.dev.food.fast.service.util.ConstantMessageExceptions;

@Component
@Order(-2)
@Log4j2
public class GlobalAuthWebExceptionHandler implements WebExceptionHandler {

    @Override
    public @NotNull Mono<Void> handle(@NotNull ServerWebExchange exchange, @NotNull Throwable ex) {

        if (ex instanceof AuthorizationTokenException) {
            if (ConstantMessageExceptions.ACCESS_TOKEN_HAS_REVOKED.equals(ex.getMessage())
                || ConstantMessageExceptions.INVALID_TOKEN.equals(ex.getMessage())
                || ConstantMessageExceptions.ACCESS_TOKEN_HAS_EXPIRED.equals(ex.getMessage())
                || ConstantMessageExceptions.ACCESS_REFRESH_TOKENS_HAVE_EXPIRED.equals(ex.getMessage())
            ) {
                return setReactiveStatus(exchange, ex.getMessage(), HttpStatus.UNAUTHORIZED);
            } else if (ConstantMessageExceptions.REFRESH_TOKEN_NOT_FOUND.equals(ex.getMessage())
                || ConstantMessageExceptions.ACCESS_TOKEN_NOT_FOUND.equals(ex.getMessage())
                || ConstantMessageExceptions.AUTHORIZATION_HEADER_IS_EMPTY.equals(ex.getMessage())) {
                return setReactiveStatus(exchange, ex.getMessage(), HttpStatus.NOT_FOUND);
            }
        } else if (ex instanceof IncorrectUserDataException) {
            if (ConstantMessageExceptions.USER_NOT_FOUND.equals(ex.getMessage())) {
                return setReactiveStatus(exchange, ex.getMessage(), HttpStatus.NOT_FOUND);
            } else if (ConstantMessageExceptions.UNSUCCESSFUL_LOGOUT.equals(ex.getMessage())) {
                return setReactiveStatus(exchange, ex.getMessage(), HttpStatus.FORBIDDEN);
            }
        }

        log.error(ex.getMessage());
        return setReactiveStatus(exchange, ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public Mono<Void> setReactiveStatus(ServerWebExchange exchange, String textMessage, HttpStatus statusCode) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(statusCode);
        response.getHeaders().setContentType(MediaType.TEXT_PLAIN);

        byte[] bytes = textMessage.getBytes();
        DataBuffer buffer = response.bufferFactory().wrap(bytes);

        return response.writeWith(Mono.just(buffer));
    }
}