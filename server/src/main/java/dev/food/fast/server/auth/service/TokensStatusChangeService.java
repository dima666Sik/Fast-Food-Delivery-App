package dev.food.fast.server.auth.service;

import dev.food.fast.server.auth.models.AccessToken;
import dev.food.fast.server.auth.models.RefreshToken;
import dev.food.fast.server.auth.models.TokenType;
import dev.food.fast.server.auth.repository.AccessTokenRepository;
import dev.food.fast.server.auth.repository.RefreshTokenRepository;
import dev.food.fast.server.auth.models.User;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokensStatusChangeService {
    private final AccessTokenRepository accessTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public void revokeAllUserRefreshTokens(User user) {
        Optional<RefreshToken> validUserRefreshTokenOptional = refreshTokenRepository.findValidRefreshTokenByUser(user.getId());

        if (validUserRefreshTokenOptional.isEmpty()) {
            deleteUserRefreshTokens(user);
            return;
        }

        var validRefreshUserToken = validUserRefreshTokenOptional.get();
        validRefreshUserToken.setExpired(true);
        validRefreshUserToken.setRevoked(true);

        refreshTokenRepository.save(validRefreshUserToken);

        deleteUserRefreshTokens(user);

    }

    public void revokeAllUserTokens(User user) {
        System.out.println("qwer");
        var validUserAccessTokensOptional = accessTokenRepository.findValidAccessTokenByUser(user.getId());
        if (validUserAccessTokensOptional.isEmpty()) {
            deleteUserTokens(user);
            return;
        }
        var validAccessUserToken = validUserAccessTokensOptional.get();
        validAccessUserToken.setExpired(true);
        validAccessUserToken.setRevoked(true);

        accessTokenRepository.save(validAccessUserToken);

        deleteUserTokens(user);
    }

    public void deleteUserTokens(User user) {
        var tokens = accessTokenRepository.findAllExpiredAndRevokedTokensByUser(user.getId());
        if (tokens.isEmpty()) {
            return;
        }
        accessTokenRepository.deleteAll(tokens);
    }

    public void deleteUserRefreshTokens(User user) {
        var refreshTokens = refreshTokenRepository.findAllExpiredAndRevokedRefreshTokensByUser(user.getId());
        if (refreshTokens.isEmpty()) {
            return;
        }
        refreshTokenRepository.deleteAll(refreshTokens);
    }

    public void saveUserToken(User user, String jwtToken) {
        var token = AccessToken.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        accessTokenRepository.save(token);
    }

    public void saveUserRefreshToken(User user, String jwtToken) {
        var token = RefreshToken.builder()
                .user(user)
                .refreshToken(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        refreshTokenRepository.save(token);
    }


    public void setStatus(@NonNull HttpServletResponse response, String textMessage, int statusCode) {
        try {
            // установка статуса
            response.setStatus(statusCode);
            response.getWriter().write(textMessage);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
