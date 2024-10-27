package ua.dev.food.fast.service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ua.dev.food.fast.service.models.AccessToken;
import ua.dev.food.fast.service.models.RefreshToken;
import ua.dev.food.fast.service.models.TokenType;
import ua.dev.food.fast.service.models.User;
import ua.dev.food.fast.service.repository.AccessTokenRepository;
import ua.dev.food.fast.service.repository.RefreshTokenRepository;

@Service
@RequiredArgsConstructor
public class TokensStatusChangeService {

    private final AccessTokenRepository accessTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public Mono<Void> expireAllUserRefreshTokens(User user) {
        return refreshTokenRepository.findValidRefreshTokenByUserId(user.getId())
                                     .flatMap(validRefreshUserToken -> {
                                         validRefreshUserToken.setExpired(true);
                                         return refreshTokenRepository.save(validRefreshUserToken).then();
                                     });
//                                     .switchIfEmpty(deleteUserRefreshTokens(user).then());
    }

    public Mono<Void> expireAllUserTokens(User user) {
        return accessTokenRepository.findValidAccessTokenByUserId(user.getId())
                                    .flatMap(validAccessUserToken -> {
                                        validAccessUserToken.setExpired(true);
                                        return accessTokenRepository.save(validAccessUserToken).then();
                                    });
//                                    .switchIfEmpty(deleteUserTokens(user).then());
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

    public Mono<Void> setReactiveStatus(ServerWebExchange exchange, String textMessage, HttpStatus statusCode) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(statusCode);
        response.getHeaders().setContentType(MediaType.TEXT_PLAIN);

        byte[] bytes = textMessage.getBytes();
        DataBuffer buffer = response.bufferFactory().wrap(bytes);

        return response.writeWith(Mono.just(buffer));
    }
}
