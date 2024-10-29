package ua.dev.food.fast.service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.ServerLogoutHandler;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ua.dev.food.fast.service.repository.AccessTokenRepository;
import ua.dev.food.fast.service.repository.RefreshTokenRepository;

@Service
@RequiredArgsConstructor
public class LogoutService implements ServerLogoutHandler {

    private final AccessTokenRepository accessTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokensHandlingService tokensHandlingService;

    @Override
    public Mono<Void> logout(WebFilterExchange exchange, Authentication authentication) {
        // Retrieve the Authorization header from the request
        return Mono.justOrEmpty(exchange.getExchange()
                .getRequest()
                .getHeaders()
                .getFirst("Authorization"))
            .filter(authHeader -> authHeader.startsWith("Bearer "))
            .map(authHeader -> authHeader.substring(7))  // Extract the JWT token
            .flatMap(accessTokenRepository::findByToken) // Find access token
            .flatMap(accessToken ->
                // Check if the access token is valid
                refreshTokenRepository.findValidRefreshTokenByUserId(accessToken.getUserId())
                    .flatMap(refreshToken -> {
                        // Expire and revoke both tokens
                        accessToken.setExpired(true);
                        accessToken.setRevoked(true);
                        refreshToken.setExpired(true);
                        refreshToken.setRevoked(true);
                        // Save both tokens reactively
                        return accessTokenRepository.save(accessToken)
                            .then(refreshTokenRepository.save(refreshToken))
                            .then();
                    }))
            .onErrorResume(e -> tokensHandlingService.setReactiveStatus(exchange.getExchange(),
                "Logout was not successful.",
                HttpStatus.FORBIDDEN));
    }
}

