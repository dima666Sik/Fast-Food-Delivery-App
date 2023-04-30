package dev.food.fast.server.auth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.food.fast.server.auth.pojo.AuthenticationResponse;
import dev.food.fast.server.auth.pojo.MessageResponse;
import dev.food.fast.server.auth.repository.UserRepository;
import dev.food.fast.server.auth.models.AccessToken;
import dev.food.fast.server.auth.models.RefreshToken;
import dev.food.fast.server.auth.models.TokenType;
import dev.food.fast.server.auth.repository.AccessTokenRepository;
import dev.food.fast.server.auth.repository.RefreshTokenRepository;
import dev.food.fast.server.auth.models.User;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Scope("prototype")
@RequiredArgsConstructor
public class ChangeStatusTokensService {
    @Autowired
    private AccessTokenRepository tokenRepository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    private final UserRepository repository;
    private final JwtService jwtService;

    public void revokeAllUserRefreshTokens(User user) {
        var validUserTokens = refreshTokenRepository.findAllValidRefreshTokenByUser(user.getId());
        if (validUserTokens.isEmpty()) {
            deleteUserRefreshTokens(user);
            return;
        }
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });

        refreshTokenRepository.saveAll(validUserTokens);

        deleteUserRefreshTokens(user);
    }

    public void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty()){
            deleteUserTokens(user);
            return;
        }
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);

        deleteUserTokens(user);
    }

    public void deleteUserTokens(User user){
        var tokens = tokenRepository.deleteAllExpiredAndRevokedTokensByUser(user.getId());
        if (tokens.isEmpty()){
            return;
        }
        tokenRepository.deleteAll(tokens);
    }

    public void deleteUserRefreshTokens(User user){
        var refreshTokens = refreshTokenRepository.deleteAllExpiredAndRevokedRefreshTokensByUser(user.getId());
        if (refreshTokens.isEmpty()){
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
        tokenRepository.save(token);
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

    public void generateNewTokensRefreshOld(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response) {
        try {
            String refreshJwt = request.getHeader("X-Refresh-Token").substring(7);
            System.out.println(refreshJwt);
            //generate new refresh & access tokens
            String userEmailFromRefreshJwt = jwtService.extractUsername(refreshJwt);
            if (userEmailFromRefreshJwt != null) {
                var user = this.repository.findByEmail(userEmailFromRefreshJwt)
                        .orElseThrow();
                if (jwtService.isTokenValid(refreshJwt, user)) {
                    var accessToken = jwtService.generateToken(user);
                    var refreshToken = jwtService.generateRefreshToken(user);

                    revokeAllUserTokens(user);
                    revokeAllUserRefreshTokens(user);

                    saveUserToken(user, accessToken);
                    saveUserRefreshToken(user, refreshToken);

                    var authResponse = AuthenticationResponse.builder()
                            .accessToken(accessToken)
                            .refreshToken(refreshToken)
                            .messageResponse(
                                    new MessageResponse("Tokens was generated new because refresh token was active.",
                                            true))
                            .build();
                    try {
                        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
                        response.setStatus(HttpServletResponse.SC_OK);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        } catch (ExpiredJwtException e2) {
            setStatus(response,
                    "Tokens all was expired. You will be much to authorization!"
                    , HttpServletResponse.SC_UNAUTHORIZED);
        }
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
