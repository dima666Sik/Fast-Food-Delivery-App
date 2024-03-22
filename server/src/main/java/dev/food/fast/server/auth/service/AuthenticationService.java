package dev.food.fast.server.auth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.food.fast.server.auth.dto.request.AuthenticationRequest;
import dev.food.fast.server.auth.dto.request.RegisterRequest;
import dev.food.fast.server.auth.dto.response.AuthenticationResponse;
import dev.food.fast.server.auth.dto.response.MessageResponse;
import dev.food.fast.server.auth.models.Role;
import dev.food.fast.server.auth.models.User;
import dev.food.fast.server.auth.repository.AccessTokenRepository;
import dev.food.fast.server.auth.repository.RefreshTokenRepository;
import dev.food.fast.server.auth.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final TokensStatusChangeService changeStatusTokensService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AccessTokenRepository accessTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public ResponseEntity<?> register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {

            return ResponseEntity.badRequest()
                    .body(MessageResponse.builder()
                            .message("User with email " + request.getEmail() + " already exists")
                            .status(false)
                            .build());
        }

        var user = User.builder()
                .firstname(request.getFirstName())
                .lastname(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getIsAdmin()?Role.ADMIN:Role.USER) // replace this "stub"
                .build();
        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        changeStatusTokensService.saveUserToken(savedUser, jwtToken);
        changeStatusTokensService.saveUserRefreshToken(user, refreshToken);

        return ResponseEntity.ok().body(AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .messageResponse(new MessageResponse("User registration " + request.getEmail() + " is successful", true))
                .build());
    }

    public ResponseEntity<?> authenticate(AuthenticationRequest request) {
        var userOptional = userRepository.findByEmail(request.getEmail());

        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(MessageResponse.builder()
                            .message("Incorrect email. User not found...")
                            .status(false)
                            .build());
        }

        var user = userOptional.get();

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.badRequest()
                    .body(MessageResponse.builder()
                            .message("Incorrect password. User not found...")
                            .status(false)
                            .build());
        }

//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        request.getEmail(),
//                        request.getPassword()
//                )
//        );

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        changeStatusTokensService.revokeAllUserTokens(user);
        changeStatusTokensService.revokeAllUserRefreshTokens(user);

        changeStatusTokensService.saveUserToken(user, jwtToken);
        changeStatusTokensService.saveUserRefreshToken(user, refreshToken);

        return ResponseEntity.ok().body(AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .messageResponse(new MessageResponse("User authorization " + request.getEmail() + " is successful", true))
                .build());
    }

    public ResponseEntity<?> refreshTokens(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt = authHeader.substring(7);

        var accessTokenOptional = accessTokenRepository.findByToken(jwt);

        System.out.println(accessTokenOptional);
        if (accessTokenOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Token not found...");
        }

        var accessToken = accessTokenOptional.get();
        var userId = accessToken.getUser().getId();

        var validRefreshUserTokensOptional = refreshTokenRepository.findValidRefreshTokenByUser(userId);
        if (validRefreshUserTokensOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Valid Refresh token was expired...");
        }
        try {
            System.out.println(validRefreshUserTokensOptional.get().getRefreshToken());
            var refreshJwt = validRefreshUserTokensOptional.get().getRefreshToken();
            //generate new refresh & access tokens
            String userEmailFromRefreshJwt = jwtService.extractUsername(refreshJwt);
            if (userEmailFromRefreshJwt != null) {
                var user = this.userRepository.findByEmail(userEmailFromRefreshJwt)
                        .orElseThrow();
                if (jwtService.isTokenValid(refreshJwt, user)) {
                    refreshTokenRepository.findByRefreshToken(validRefreshUserTokensOptional.get().getRefreshToken()).ifPresent(rt -> {
                        if (!rt.isExpired() && !rt.isRevoked()) {
                            rt.setExpired(true);
                            rt.setRevoked(true);
                            refreshTokenRepository.save(rt);

                            var newAccessToken = jwtService.generateToken(user);
                            var newRefreshToken = jwtService.generateRefreshToken(user);

                            changeStatusTokensService.revokeAllUserTokens(user);
                            changeStatusTokensService.revokeAllUserRefreshTokens(user);

                            changeStatusTokensService.saveUserToken(user, newAccessToken);
                            changeStatusTokensService.saveUserRefreshToken(user, newRefreshToken);

                            var authResponse = AuthenticationResponse.builder()
                                    .accessToken(newAccessToken)
                                    .messageResponse(
                                            new MessageResponse("Tokens was expired and generated new because refresh token was active.",
                                                    true))
                                    .build();
                            try {

                                response.setStatus(HttpServletResponse.SC_OK);
                                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                                ObjectMapper objectMapper = new ObjectMapper();
                                objectMapper.writeValue(response.getWriter(), authResponse);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        } catch (ExpiredJwtException e) {
            var validAccessUserToken = accessTokenOptional.get();
            validAccessUserToken.setExpired(true);
            validAccessUserToken.setRevoked(true);

            accessTokenRepository.save(validAccessUserToken);

            var validRefreshUserToken = validRefreshUserTokensOptional.get();
            validRefreshUserToken.setExpired(true);
            validRefreshUserToken.setRevoked(true);

            refreshTokenRepository.save(validRefreshUserToken);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You need to reauthorize! Tokens all were expired. You will be much to authorization!");
        }
        return null;
    }
}
