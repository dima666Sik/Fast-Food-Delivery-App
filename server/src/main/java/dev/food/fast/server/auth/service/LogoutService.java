package dev.food.fast.server.auth.service;

import dev.food.fast.server.auth.repository.AccessTokenRepository;
import dev.food.fast.server.auth.repository.RefreshTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final AccessTokenRepository accessTokenRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        jwt = authHeader.substring(7);

        var accessTokenOptional = accessTokenRepository.findByToken(jwt);

        System.out.println(accessTokenOptional);
        if (accessTokenOptional.isEmpty()) {
            return;
        }

        var accessToken = accessTokenOptional.get();

        var validRefreshUserTokensOptional = refreshTokenRepository
                .findValidRefreshTokenByUser(accessToken.getUser().getId());
        if (validRefreshUserTokensOptional.isEmpty()) {
            return;
        }
        var refreshToken = validRefreshUserTokensOptional.get();

        if (accessToken.getToken() != null && refreshToken.getRefreshToken() != null) {
            accessToken.setExpired(true);
            accessToken.setRevoked(true);

            refreshToken.setExpired(true);
            refreshToken.setRevoked(true);

            accessTokenRepository.save(accessToken);
            refreshTokenRepository.save(refreshToken);

            SecurityContextHolder.clearContext();
        }
    }
}
