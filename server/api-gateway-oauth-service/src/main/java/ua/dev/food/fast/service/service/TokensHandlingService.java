package ua.dev.food.fast.service.service;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import ua.dev.food.fast.service.domain.model.AccessToken;
import ua.dev.food.fast.service.domain.model.RefreshToken;
import ua.dev.food.fast.service.domain.model.TokenType;
import ua.dev.food.fast.service.domain.model.User;
import ua.dev.food.fast.service.exception_handler.exception.AuthorizationTokenException;
import ua.dev.food.fast.service.exception_handler.exception.IncorrectUserDataException;
import ua.dev.food.fast.service.repository.AccessTokenRepository;
import ua.dev.food.fast.service.repository.RefreshTokenRepository;
import ua.dev.food.fast.service.util.ConstantMessageExceptions;

@Service
@RequiredArgsConstructor
public class TokensHandlingService {

    private final AccessTokenRepository accessTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final ReactiveUserDetailsService userDetailsService;
    private final JwtService jwtService;

    public Mono<Void> expireAllUserRefreshTokens(User user) {
        return refreshTokenRepository.findValidRefreshTokenByUserId(user.getId())
            .flatMap(validRefreshUserToken -> {
                validRefreshUserToken.setExpired(true);
                return refreshTokenRepository.save(validRefreshUserToken).then();
            });
    }

    public Mono<Void> expireAllUserTokens(User user) {
        return accessTokenRepository.findValidAccessTokenByUserId(user.getId())
            .flatMap(validAccessUserToken -> {
                validAccessUserToken.setExpired(true);
                return accessTokenRepository.save(validAccessUserToken).then();
            });
    }

    public Mono<Void> deleteUserTokens() {
        return accessTokenRepository.findAllExpiredAndRevokedTokens()
            .collectList()
            .flatMap(tokens -> {
                if (tokens.isEmpty()) {
                    return Mono.empty();
                }
                return accessTokenRepository.deleteAll(tokens);
            });
    }

    public Mono<Void> deleteUserRefreshTokens() {
        return refreshTokenRepository.findAllExpiredAndRevokedRefreshTokens()
            .collectList()
            .flatMap(refreshTokens -> {
                if (refreshTokens.isEmpty()) {
                    return Mono.empty();
                }
                return refreshTokenRepository.deleteAll(refreshTokens);
            });
    }

    public Mono<AccessToken> saveUserToken(User user, String jwtToken) {
        var token = AccessToken.builder()
            .userId(user.getId())
            .token(jwtToken)
            .tokenType(TokenType.BEARER)
            .isExpired(false)
            .isRevoked(false)
            .build();
        return accessTokenRepository.save(token);
    }

    public Mono<RefreshToken> saveUserRefreshToken(User user, String jwtToken) {
        var token = RefreshToken.builder()
            .userId(user.getId())
            .token(jwtToken)
            .tokenType(TokenType.BEARER)
            .isExpired(false)
            .isRevoked(false)
            .build();
        return refreshTokenRepository.save(token);
    }

    public Mono<Void> handleValidToken(String jwt, ServerWebExchange exchange, WebFilterChain chain) {
        String userEmail = jwtService.extractUsername(jwt);

        return userDetailsService.findByUsername(userEmail)
            .flatMap(userDetails -> {
                if (jwtService.isTokenValid(jwt, userDetails)) {

                    UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    return chain.filter(exchange)
                        .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authToken));
                }
                return Mono.error(new AuthorizationTokenException(ConstantMessageExceptions.INVALID_TOKEN));
            })
            .switchIfEmpty(Mono.error(new IncorrectUserDataException(ConstantMessageExceptions.USER_NOT_FOUND)));
    }

    public Mono<Void> handleExpiredToken(String jwt) {
        return accessTokenRepository.findByToken(jwt)
            .flatMap(accessToken -> {

                var userId = accessToken.getUserId();
                return refreshTokenRepository.findValidRefreshTokenByUserId(userId)
                    .flatMap(refreshToken -> {
                        jwtService.extractUsername(refreshToken.getToken());
                        return Mono.error(new AuthorizationTokenException(ConstantMessageExceptions.ACCESS_TOKEN_HAS_EXPIRED));
                    })
                    .switchIfEmpty(Mono.error(new AuthorizationTokenException(ConstantMessageExceptions.REFRESH_TOKEN_NOT_FOUND)))
                    .onErrorResume(ExpiredJwtException.class, e ->
                        Mono.error(new AuthorizationTokenException(ConstantMessageExceptions.ACCESS_REFRESH_TOKENS_HAVE_EXPIRED)))
                    .then();
            })
            .switchIfEmpty(Mono.error(new AuthorizationTokenException(ConstantMessageExceptions.ACCESS_TOKEN_NOT_FOUND)));
    }
}
