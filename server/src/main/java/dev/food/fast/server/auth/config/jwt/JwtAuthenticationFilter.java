package dev.food.fast.server.auth.config.jwt;

import dev.food.fast.server.auth.service.TokensStatusChangeService;
import dev.food.fast.server.auth.service.JwtService;
import dev.food.fast.server.auth.repository.AccessTokenRepository;
import dev.food.fast.server.auth.repository.RefreshTokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final AccessTokenRepository accessTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokensStatusChangeService tokensStatusChangeService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        if (request.getServletPath().contains("/api/v1/auth") || request.getServletPath().contains("/api/v1/slider") ||
                request.getServletPath().contains("/public/images") || request.getServletPath().contains("/api/v1/foods") ||
                request.getServletPath().contains("/api/v1/email/") ||
                request.getServletPath().contains("/api/v1/order-purchase/")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            tokensStatusChangeService.setStatus(response,
                    "You are not authorization! Authorization header is empty!"
                    , HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        jwt = authHeader.substring(7);
        var isTokenExist = accessTokenRepository.findByToken(jwt);
        if (isTokenExist.isEmpty()) {
            tokensStatusChangeService.setStatus(response,
                    "Tokens from client is bad!"
                    , HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        try {
            userEmail = jwtService.extractUsername(jwt);

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                var isTokenValid = isTokenExist
                        .map(t -> !t.isExpired() && !t.isRevoked())
                        .orElse(false);
                if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    filterChain.doFilter(request, response);
                }
            }
        } catch (ExpiredJwtException e) {

            var accessTokenOptional = accessTokenRepository.findByToken(jwt);
            if (accessTokenOptional.isEmpty()) {
                tokensStatusChangeService.setStatus(response, "User not found...", HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            var accessToken = accessTokenOptional.get();
            var userId = accessToken.getUser().getId();

            var validRefreshUserTokensOptional = refreshTokenRepository.findValidRefreshTokenByUser(userId);
            if (validRefreshUserTokensOptional.isEmpty()) {
                tokensStatusChangeService.setStatus(response, "Valid Refresh token was expired...", HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            accessTokenRepository.findByToken(jwt).ifPresent(t -> {

                try {
                    jwtService.extractUsername(validRefreshUserTokensOptional.get().getRefreshToken());
                    tokensStatusChangeService.setStatus(response,
                            "Access token was expired! Refresh is valid!"
                            , HttpServletResponse.SC_UNAUTHORIZED);

                } catch (ExpiredJwtException e1) {

                    refreshTokenRepository.findByRefreshToken(validRefreshUserTokensOptional.get().getRefreshToken()).ifPresent(rt -> {
                        if (!rt.isExpired() && !rt.isRevoked()) {
                            tokensStatusChangeService.setStatus(response,
                                    "All Tokens (access & refresh) were expired! Please generate news tokens!"
                                    , HttpServletResponse.SC_UNAUTHORIZED);
                        }
                    });
                }

            });
        }
    }
}