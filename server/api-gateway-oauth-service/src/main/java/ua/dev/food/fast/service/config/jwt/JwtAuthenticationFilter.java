package ua.dev.food.fast.service.config.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import ua.dev.food.fast.service.models.AccessToken;
import ua.dev.food.fast.service.repository.AccessTokenRepository;
import ua.dev.food.fast.service.repository.RefreshTokenRepository;
import ua.dev.food.fast.service.service.JwtService;
import ua.dev.food.fast.service.service.TokensStatusChangeService;


@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements WebFilter {

    private final AccessTokenRepository accessTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;
    private final ReactiveUserDetailsService userDetailsService;
    private final TokensStatusChangeService tokensStatusChangeService;

    @Override
    public @NonNull Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        String path = exchange.getRequest()
                              .getPath()
                              .toString();

        // Allow specific paths without authentication
        if (path.contains("/api/v1/auth") ||
                path.contains("/api/v1/slider") ||
                path.contains("/public/images") ||
                path.contains("/api/v1/foods") ||
                path.contains("/api/v1/email/") ||
                path.contains("/api/v1/order-purchase/")) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest()
                                    .getHeaders()
                                    .getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return tokensStatusChangeService.setReactiveStatus(exchange,
                    "Authorization header is empty!",
                    HttpStatus.NOT_FOUND);
        }

        String jwt = authHeader.substring(7);

        return accessTokenRepository.findByToken(jwt)
                                    .flatMap(token -> {
                                        if (token.isExpired() || token.isRevoked()) {
                                            return tokensStatusChangeService.setReactiveStatus(exchange,
                                                    "Tokens from client are invalid!",
                                                    HttpStatus.NOT_FOUND);
                                        }
                                        return handleValidToken(jwt, exchange, chain);
                                    })
                                    .switchIfEmpty(tokensStatusChangeService.setReactiveStatus(exchange,
                                            "Access token " + jwt + " from client was not found",
                                            HttpStatus.NOT_FOUND))
                                    .onErrorResume(ExpiredJwtException.class, e -> handleExpiredToken(jwt, exchange));
    }

    private Mono<Void> handleValidToken(String jwt, ServerWebExchange exchange, WebFilterChain chain) {
        String userEmail = jwtService.extractUsername(jwt);

        return userDetailsService.findByUsername(userEmail)
                                 .flatMap(userDetails -> {
                                     if (jwtService.isTokenValid(jwt, userDetails)) {
                                         UsernamePasswordAuthenticationToken authToken =
                                                 new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                                         return chain.filter(exchange)
                                                     .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authToken));
                                     }
                                     return tokensStatusChangeService.setReactiveStatus(exchange,
                                             "Token is not valid",
                                             HttpStatus.UNAUTHORIZED);
                                 });
    }

    private Mono<Void> handleExpiredToken(String jwt, ServerWebExchange exchange) {
        return accessTokenRepository.findByToken(jwt)
                                    .flatMap(accessToken -> {
                                        var userId = accessToken.getUserId();
                                        return refreshTokenRepository.findValidRefreshTokenByUserId(userId)
                                                                     .flatMap(refreshToken -> {
                                                                         if (!refreshToken.isExpired() && !refreshToken.isRevoked()) {
                                                                             return tokensStatusChangeService.setReactiveStatus(exchange,
                                                                                     "Access token was expired! Refresh is valid!",
                                                                                     HttpStatus.UNAUTHORIZED);
                                                                         }
                                                                         return tokensStatusChangeService.setReactiveStatus(exchange,
                                                                                 "All Tokens (access & refresh) were expired! Please generate new tokens!",
                                                                                 HttpStatus.UNAUTHORIZED);
                                                                     });
                                    })
                                    .switchIfEmpty(tokensStatusChangeService.setReactiveStatus(exchange,
                                            "User not found...",
                                            HttpStatus.NOT_FOUND));
    }
}