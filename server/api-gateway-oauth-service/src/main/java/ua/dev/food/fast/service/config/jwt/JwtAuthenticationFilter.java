package ua.dev.food.fast.service.config.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import ua.dev.food.fast.service.repository.AccessTokenRepository;
import ua.dev.food.fast.service.service.TokensHandlingService;
import ua.dev.food.fast.service.util.ConstantMessageExceptions;

import java.util.List;


@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements WebFilter {
    @Value("${back-end.security.custom.allowed.paths}")
    private List<String> allowedPaths;
    private final AccessTokenRepository accessTokenRepository;
    private final TokensHandlingService tokensHandlingService;

    @Override
    public @NonNull Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        String path = exchange.getRequest().getPath().toString();

        // Allow specific paths without authentication
        if (allowedPaths.stream().anyMatch(path::contains)) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith(ConstantMessageExceptions.BEARER_HEADER)) {
            return tokensHandlingService.setReactiveStatus(exchange, ConstantMessageExceptions.AUTHORIZATION_HEADER_IS_EMPTY, HttpStatus.NOT_FOUND);
        }

        String jwt = authHeader.substring(7);

        return accessTokenRepository.findByToken(jwt).flatMap(token -> {
                if (token.isExpired()) {
                    return tokensHandlingService.setReactiveStatus(exchange, ConstantMessageExceptions.ACCESS_TOKEN_HAS_EXPIRED, HttpStatus.UNAUTHORIZED);
                } else if (token.isRevoked()) {
                    return tokensHandlingService.setReactiveStatus(exchange, ConstantMessageExceptions.ACCESS_TOKEN_HAS_REVOKED, HttpStatus.UNAUTHORIZED);
                } else return tokensHandlingService.handleValidToken(jwt, exchange, chain);
            })
            .switchIfEmpty(tokensHandlingService.setReactiveStatus(exchange, ConstantMessageExceptions.ACCESS_TOKEN_NOT_FOUND, HttpStatus.NOT_FOUND))
            .onErrorResume(ExpiredJwtException.class, e -> tokensHandlingService.handleExpiredToken(jwt, exchange));
    }


}