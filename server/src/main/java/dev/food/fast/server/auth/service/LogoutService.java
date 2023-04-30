package dev.food.fast.server.auth.service;

import dev.food.fast.server.auth.repository.AccessTokenRepository;
import dev.food.fast.server.auth.repository.RefreshTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final AccessTokenRepository tokenRepository;

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
        var storedToken = tokenRepository.findByToken(jwt)
                .orElse(null);
        var storedRefreshToken = refreshTokenRepository.findByRefreshToken(jwt)
                .orElse(null);
        if (storedToken != null && storedRefreshToken != null) {
            storedToken.setExpired(true);
            storedToken.setRevoked(true);

            storedRefreshToken.setExpired(true);
            storedRefreshToken.setRevoked(true);

            tokenRepository.save(storedToken);
            refreshTokenRepository.save(storedRefreshToken);

            SecurityContextHolder.clearContext();
        }
    }
}
