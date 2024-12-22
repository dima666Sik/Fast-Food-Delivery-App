package ua.dev.food.fast.service.config.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import ua.dev.food.fast.service.exception_handler.exception.AuthorizationTokenException;
import ua.dev.food.fast.service.repository.AccessTokenRepository;
import ua.dev.food.fast.service.service.TokensHandlingService;
import ua.dev.food.fast.service.util.ConstantMessageExceptions;

import java.util.List;
import java.util.Optional;


@Component
@RequiredArgsConstructor
@Log4j2
public class JwtAuthenticationFilter implements WebFilter {
    @Value("${back-end.security.custom.allowed.paths}")
    private List<String> allowedPaths;
    private final AccessTokenRepository accessTokenRepository;
    private final TokensHandlingService tokensHandlingService;

    @Override
    public @NonNull Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        String path = exchange.getRequest().getPath().toString();
        log.info(path);
        // Allow specific paths without authentication
        if (allowedPaths.stream().anyMatch(path::contains)) {
            return chain.filter(exchange);
        }

        Optional<String> authHeader = Optional.ofNullable(exchange.getRequest().getHeaders().getFirst("Authorization"));
        log.info("Authentication Header: " + authHeader);
        if (authHeader.isEmpty() || !authHeader.get().startsWith(ConstantMessageExceptions.BEARER_HEADER)) {
            return Mono.error(new AuthorizationTokenException(ConstantMessageExceptions.AUTHORIZATION_HEADER_IS_EMPTY));
        }

        String jwt = authHeader.get().substring(7);
        log.info(jwt);
        return accessTokenRepository.findByToken(jwt)
            .switchIfEmpty(Mono.error(new AuthorizationTokenException(ConstantMessageExceptions.ACCESS_TOKEN_NOT_FOUND)))
            .flatMap(token -> {
                log.info("Access token is ->");
                if (token.isExpired()) {
                    log.info("expired");
                    return Mono.error(new AuthorizationTokenException(ConstantMessageExceptions.ACCESS_TOKEN_HAS_EXPIRED));
                } else if (token.isRevoked()) {
                    log.info("revoking token");
                    return Mono.error(new AuthorizationTokenException(ConstantMessageExceptions.ACCESS_TOKEN_HAS_REVOKED));
                }
                log.info("good token");
                return tokensHandlingService.handleValidToken(jwt, exchange, chain);
            })
            .onErrorResume(ExpiredJwtException.class, e -> {
                log.info("Expired token");
                return tokensHandlingService.handleExpiredToken(jwt);
            });
    }


}