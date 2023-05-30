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
import org.springframework.scheduling.annotation.Async;
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
                request.getServletPath().contains("/public/images") || request.getServletPath().contains("/api/v1/foods") ||
                request.getServletPath().contains("/api/v1/email/") ||
                request.getServletPath().contains("/api/v1/private/order-purchase/add-order-with-purchase-guest")) {
            filterChain.doFilter(request, response);
            return;
        }
        final String authHeader = request.getHeader("Authorization");
        System.out.println("Authorization"+ authHeader);
        final String jwt;
        final String userEmail;
        System.out.println("Liiiiip1");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            changeStatusTokensService.setStatus(response,
                    "You are not authorization! Authorization header is empty!"
                    , HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        jwt = authHeader.substring(7);
        System.out.println("Liiiiip2");
        var isTokenExist = tokenRepository.findByToken(jwt);
        if (isTokenExist.isEmpty()) {
            changeStatusTokensService.setStatus(response,
                    "Tokens from client is bad!"
                    , HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        System.out.println("Liiiiip3");
        try {
            userEmail = jwtService.extractUsername(jwt);
            System.out.println("Liiiiip4");
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                var isTokenValid = isTokenExist
                        .map(t -> !t.isExpired() && !t.isRevoked())
                        .orElse(false);
                System.out.println("Liiiiip5");
                if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    System.out.println("Liiiiip6");
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    filterChain.doFilter(request, response);
                    System.out.println("Liiiiip7");
                    return;
                }
            }
            System.out.println("Liiiiip8");
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            System.out.println("Heklp1");
            var accessTokenOptional = tokenRepository.findByToken(jwt);
            if (accessTokenOptional.isEmpty()) {
                changeStatusTokensService.setStatus(response, "User not found...", HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            System.out.println("Heklp2");
            var accessToken = accessTokenOptional.get();
            var userId = accessToken.getUser().getId();

            var validRefreshUserTokensOptional = refreshTokenRepository.findValidRefreshTokenByUser(userId);
            if (validRefreshUserTokensOptional.isEmpty()) {
                changeStatusTokensService.setStatus(response, "Valid Refresh token was expired...", HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            System.out.println("Heklp3");
            tokenRepository.findByToken(jwt).ifPresent(t -> {
                if (!t.isExpired() && !t.isRevoked()) {
                    t.setExpired(true);
                    t.setRevoked(true);
                    tokenRepository.save(t);
                }
                System.out.println("Heklp4");
                var refreshToken = validRefreshUserTokensOptional.get().getRefreshToken();
                refreshTokenRepository.findByRefreshToken(refreshToken).ifPresent(rt -> {
                    if (!rt.isExpired() && !rt.isRevoked()) {
                        rt.setExpired(true);
                        rt.setRevoked(true);
                        refreshTokenRepository.save(rt);
                        System.out.println("Heklp5");
                        changeStatusTokensService.generateNewTokensRefreshOld(refreshToken, response);
                    }
                });
            });
        }
    }
}