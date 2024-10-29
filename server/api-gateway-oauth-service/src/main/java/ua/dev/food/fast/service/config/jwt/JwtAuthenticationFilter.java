package ua.dev.food.fast.service.config.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import ua.dev.food.fast.service.repository.AccessTokenRepository;
import ua.dev.food.fast.service.service.TokensHandlingService;


@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements WebFilter {

    private final AccessTokenRepository accessTokenRepository;
    private final TokensHandlingService tokensHandlingService;

    @Override
    public @NonNull Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        String path = exchange.getRequest()
            .getPath()
            .toString();

        // Allow specific paths without authentication
        if (path.contains("/api/v2/auth") ||
            path.contains("/api/v2/slider") ||
            path.contains("/public/images") ||
            path.contains("/api/v2/foods") ||
            path.contains("/api/v2/email/") ||
            path.contains("/api/v2/order-purchase/")) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest()
            .getHeaders()
            .getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return tokensHandlingService.setReactiveStatus(exchange,
                "Authorization header is empty!",
                HttpStatus.NOT_FOUND);
        }

        String jwt = authHeader.substring(7);

        return accessTokenRepository.findByToken(jwt)
            .flatMap(token -> {
                if (token.isExpired() || token.isRevoked()) {
                    return tokensHandlingService.setReactiveStatus(exchange,
                        "Tokens from client are invalid!",
                        HttpStatus.NOT_FOUND);
                }
                return tokensHandlingService.handleValidToken(jwt, exchange, chain);
            })
            .switchIfEmpty(tokensHandlingService.setReactiveStatus(exchange,
                "Access token " + jwt + " from client was not found",
                HttpStatus.NOT_FOUND))
            .onErrorResume(ExpiredJwtException.class, e -> tokensHandlingService.handleExpiredToken(jwt, exchange));
    }


}