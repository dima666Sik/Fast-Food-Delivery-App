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
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@Scope("prototype")
@RequiredArgsConstructor
public class ChangeStatusTokensService {
    @Autowired
    private AccessTokenRepository accessTokenRepository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    private final UserRepository userRepository;
    private final JwtService jwtService;

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
        var validUserAccessTokensOptional = accessTokenRepository.findValidAccessTokenByUser(user.getId());
        if (validUserAccessTokensOptional.isEmpty()){
            deleteUserTokens(user);
            return;
        }
        var validAccessUserToken = validUserAccessTokensOptional.get();
        validAccessUserToken.setExpired(true);
        validAccessUserToken.setRevoked(true);

        accessTokenRepository.save(validAccessUserToken);

        deleteUserTokens(user);
    }

    public void deleteUserTokens(User user){
        var tokens = accessTokenRepository.deleteAllExpiredAndRevokedTokensByUser(user.getId());
        if (tokens.isEmpty()){
            return;
        }
        accessTokenRepository.deleteAll(tokens);
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

    public void generateNewTokensRefreshOld(String refreshJwt, @NonNull HttpServletResponse response) {
        try {
            System.out.println(refreshJwt);
            //generate new refresh & access tokens
            System.out.println("Heklp6");
            String userEmailFromRefreshJwt = jwtService.extractUsername(refreshJwt);
            if (userEmailFromRefreshJwt != null) {
                var user = this.userRepository.findByEmail(userEmailFromRefreshJwt)
                        .orElseThrow();
                System.out.println("Heklp7");
                if (jwtService.isTokenValid(refreshJwt, user)) {
                    var accessToken = jwtService.generateToken(user);
                    var refreshToken = jwtService.generateRefreshToken(user);
                    System.out.println("Heklp8");
                    revokeAllUserTokens(user);
                    revokeAllUserRefreshTokens(user);

                    saveUserToken(user, accessToken);
                    saveUserRefreshToken(user, refreshToken);
                    System.out.println("Heklp9");
                    var authResponse = AuthenticationResponse.builder()
                            .accessToken(accessToken)
                            .messageResponse(
                                    new MessageResponse("Tokens was expired and generated new because refresh token was active.",
                                            true))
                            .build();
                    try {
                        System.out.println("Heklp10");
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                        ObjectMapper objectMapper = new ObjectMapper();
                        objectMapper.writeValue(response.getWriter(), authResponse);
                        System.out.println("Heklp11");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        } catch (ExpiredJwtException e) {
            setStatus(response,
                    "You need to reauthorize! Tokens all were expired. You will be much to authorization!"
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
