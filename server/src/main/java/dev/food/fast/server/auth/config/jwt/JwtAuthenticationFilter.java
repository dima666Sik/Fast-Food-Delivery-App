package dev.food.fast.server.auth.config.jwt;

import dev.food.fast.server.auth.service.ChangeStatusTokensService;
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
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private final AccessTokenRepository tokenRepository;
    @Autowired
    private final RefreshTokenRepository refreshTokenRepository;

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final ChangeStatusTokensService changeStatusTokensService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        if (request.getServletPath().contains("/api/v1/auth") || request.getServletPath().contains("/api/v1/slider") ||
                request.getServletPath().contains("/public/images") || request.getServletPath().contains("/api/v1/foods")) {
            filterChain.doFilter(request, response);
            return;
        }
        final String authHeader = request.getHeader("Authorization");
        final String authHeaderRefreshToken = request.getHeader("X-Refresh-Token");
        final String jwt;
        final String refreshJwt;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ") ||
                authHeaderRefreshToken == null || !authHeaderRefreshToken.startsWith("Bearer ")) {
            changeStatusTokensService.setStatus(response,
                    "You are not authorization!"
                    , HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        jwt = authHeader.substring(7);
        refreshJwt = authHeaderRefreshToken.substring(7);
        var isTokenExist = tokenRepository.findByToken(jwt);
        var isRefreshTokenExist = refreshTokenRepository.findByRefreshToken(refreshJwt);
        if (isTokenExist.isEmpty() || isRefreshTokenExist.isEmpty()) {
            changeStatusTokensService.setStatus(response,
                    "Tokens from client is bad!"
                    , HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        try {
            userEmail = jwtService.extractUsername(jwt);
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                var isTokenValid = isTokenExist
                        .map(t -> !t.isExpired() && !t.isRevoked())
                        .orElse(false);
                var isRefreshTokenValid = isRefreshTokenExist
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
                }
                if (jwtService.isTokenValid(refreshJwt, userDetails) && isRefreshTokenValid) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            tokenRepository.findByToken(jwt).ifPresent(t -> {
                if (!t.isExpired() && !t.isRevoked()) {
                    t.setExpired(true);
                    t.setRevoked(true);
                    tokenRepository.save(t);
                }

                refreshTokenRepository.findByRefreshToken(refreshJwt).ifPresent(rt -> {
                    if (!rt.isExpired() && !rt.isRevoked()) {
                        rt.setExpired(true);
                        rt.setRevoked(true);
                        refreshTokenRepository.save(rt);

                        changeStatusTokensService.generateNewTokensRefreshOld(request,response);
                    } else {
                        changeStatusTokensService.setStatus(response,
                                "Tokens all was expired. You will be much to authorization!"
                                , HttpServletResponse.SC_UNAUTHORIZED);
                    }
                });
            });
        }
    }
}
