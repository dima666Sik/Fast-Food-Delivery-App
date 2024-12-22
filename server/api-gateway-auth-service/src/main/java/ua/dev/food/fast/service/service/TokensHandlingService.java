package ua.dev.food.fast.service.service;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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

/***
 * Service for handling operations related to access and refresh tokens.
 * Provides functionality for managing token expiration, deletion, and validation.
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class TokensHandlingService {

    private final AccessTokenRepository accessTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final ReactiveUserDetailsService userDetailsService;
    private final JwtService jwtService;

    /**
     * Expires all valid refresh tokens associated with the specified user.
     *
     * @param user the user whose refresh tokens should be expired
     * @return a {@link Mono<Void>} indicating completion
     */
    public Mono<Void> expireAllUserRefreshTokens(User user) {
        return refreshTokenRepository.findValidRefreshTokenByUserId(user.getId())
            .flatMap(validRefreshUserToken -> {
                validRefreshUserToken.setExpired(true);
                return refreshTokenRepository.save(validRefreshUserToken).then();
            });
    }

    /**
     * Expires all valid access tokens associated with the specified user.
     *
     * @param user the user whose access tokens should be expired
     * @return a {@link Mono<Void>} indicating completion
     */
    public Mono<Void> expireAllUserTokens(User user) {
        return accessTokenRepository.findValidAccessTokenByUserId(user.getId())
            .flatMap(validAccessUserToken -> {
                validAccessUserToken.setExpired(true);
                return accessTokenRepository.save(validAccessUserToken).then();
            });
    }

    /**
     * Deletes all expired and revoked access tokens.
     *
     * @return a {@link Mono<Void>} indicating completion
     */
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

    /**
     * Deletes all expired and revoked refresh tokens.
     *
     * @return a {@link Mono<Void>} indicating completion
     */
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

    /**
     * Saves a new access token for the specified user.
     *
     * @param user     the user for whom the token is being saved
     * @param jwtToken the JWT string representing the token
     * @return a {@link Mono<AccessToken>} containing the saved token
     */
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

    /**
     * Saves a new refresh token for the specified user.
     *
     * @param user     the user for whom the token is being saved
     * @param jwtToken the JWT string representing the token
     * @return a {@link Mono<RefreshToken>} containing the saved token
     */
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

    /**
     * Handles a valid token by authenticating the user and continuing the filter chain.
     *
     * @param jwt      the JWT token to validate
     * @param exchange the current server web exchange
     * @param chain    the filter chain to continue processing
     * @return a {@link Mono<Void>} indicating completion or an error
     */
    public Mono<Void> handleValidToken(String jwt, ServerWebExchange exchange, WebFilterChain chain) {
        try {
            String userEmail = jwtService.extractUsername(jwt);
            return userDetailsService.findByUsername(userEmail)
                .doOnNext(user -> log.info("Founded user: " + user.getUsername()))
                .switchIfEmpty(Mono.error(new IncorrectUserDataException(ConstantMessageExceptions.USER_NOT_FOUND)))
                .flatMap(userDetails -> {
                    if (jwtService.isTokenValid(jwt, userDetails)) {
                        UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                        return chain.filter(exchange)
                            .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authToken));
                    }
                    return Mono.error(new AuthorizationTokenException(ConstantMessageExceptions.INVALID_TOKEN));
                });
        } catch (ExpiredJwtException e) {
            return Mono.error(new AuthorizationTokenException(ConstantMessageExceptions.ACCESS_TOKEN_HAS_EXPIRED));
        }
    }

    /**
     * Handles an expired access token by checking for a valid refresh token and taking appropriate actions.
     *
     * @param jwt the expired JWT token
     * @return a {@link Mono<Void>} indicating completion or an error
     */
    public Mono<Void> handleExpiredToken(String jwt) {
        return accessTokenRepository.findByToken(jwt)
            .flatMap(accessToken -> {

                var userId = accessToken.getUserId();
                return refreshTokenRepository.findValidRefreshTokenByUserId(userId)
                    .switchIfEmpty(Mono.error(new AuthorizationTokenException(ConstantMessageExceptions.REFRESH_TOKEN_NOT_FOUND)))
                    .flatMap(refreshToken -> {
                        jwtService.extractUsername(refreshToken.getToken());
                        return Mono.error(new AuthorizationTokenException(ConstantMessageExceptions.ACCESS_TOKEN_HAS_EXPIRED));
                    })
                    .onErrorResume(ExpiredJwtException.class, e ->
                        Mono.error(new AuthorizationTokenException(ConstantMessageExceptions.ACCESS_REFRESH_TOKENS_HAVE_EXPIRED)))
                    .then();
            })
            .switchIfEmpty(Mono.error(new AuthorizationTokenException(ConstantMessageExceptions.ACCESS_TOKEN_NOT_FOUND)));
    }
}
