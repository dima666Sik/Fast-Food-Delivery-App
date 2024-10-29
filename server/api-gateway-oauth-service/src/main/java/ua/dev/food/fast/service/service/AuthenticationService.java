package ua.dev.food.fast.service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.dev.food.fast.service.domain.dto.request.AuthenticationRequest;
import ua.dev.food.fast.service.domain.dto.request.RegisterRequest;
import ua.dev.food.fast.service.domain.dto.response.AuthenticationResponse;
import ua.dev.food.fast.service.domain.dto.response.MessageResponse;
import ua.dev.food.fast.service.domain.model.Permission;
import ua.dev.food.fast.service.domain.model.Role;
import ua.dev.food.fast.service.domain.model.User;
import ua.dev.food.fast.service.domain.model.UserPermission;
import ua.dev.food.fast.service.repository.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final TokensHandlingService changeStatusTokensService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AccessTokenRepository accessTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;
    private final UserPermissionRepository userPermissionRepository;

    @Transactional
    public Mono<ResponseEntity<MessageResponse>> register(RegisterRequest request) {
        return userRepository.findByEmail(request.getEmail()).flatMap(user -> Mono.just(ResponseEntity.badRequest()
                .body(MessageResponse.builder()
                    .message("User with email " + request.getEmail() + " already exists")
                    .status(false)
                    .build())))
            .switchIfEmpty(Mono.defer(() -> {
                var user = User.builder().firstname(request.getFirstName())
                    .lastname(request.getLastName()).email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .permissions(List.of(Permission.builder().role(Role.USER).build()))
                    .build();
                return userRepository.save(user).flatMap(savedUser -> {
                    List<Mono<UserPermission>> listUserPermissionsMonos = user.getPermissions()
                        .stream()
                        .map(permission -> permissionRepository.findPermissionByRole(permission.getRole()
                                .name())
                            .map(foundPermission -> UserPermission.builder()
                                .userId(user.getId())
                                .permissionId(foundPermission.getId())
                                .build()))
                        .toList();

                    var jwtToken = jwtService.generateToken(savedUser);
                    var refreshToken = jwtService.generateRefreshToken(savedUser);

                    // Combine all Mono<UserPermission> into a Flux<UserPermission> and save each one
                    Flux<UserPermission> permissionsFlux = Flux.fromIterable(listUserPermissionsMonos)
                        .flatMap(mono -> mono);

                    return permissionsFlux.flatMap(userPermissionRepository::save) // Save each UserPermission reactively
                        .then(changeStatusTokensService.saveUserToken(savedUser, jwtToken))
                        .then(changeStatusTokensService.saveUserRefreshToken(savedUser, refreshToken))
                        .thenReturn(ResponseEntity.status(HttpStatus.CREATED)
                            .body(MessageResponse.builder()
                                .message("User registration " + request.getEmail() + " is successful")
                                .status(true)
                                .mainResponse(AuthenticationResponse.builder()
                                    .accessToken(jwtToken)
                                    .refreshToken(refreshToken)
                                    .build())
                                .build()));
                });
            }));
    }

    public Mono<ResponseEntity<MessageResponse>> authenticate(AuthenticationRequest request) {
        return userRepository.findByEmail(request.getEmail()).flatMap(user -> {
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return Mono.just(ResponseEntity.badRequest()
                    .body(MessageResponse.builder().message("Incorrect password.")
                        .status(false).build()));
            }

            return userPermissionRepository.findAllByUserId(user.getId())
                .flatMap(userPermission -> permissionRepository.findById(userPermission.getPermissionId())) // Find permissions reactively
                .collectList() // Collect results into a single Mono<List<Permission>>
                .flatMap(permissions -> {
                    // Set permissions on the user
                    user.setPermissions(permissions);

                    // Generate tokens
                    var jwtToken = jwtService.generateToken(user);
                    var refreshToken = jwtService.generateRefreshToken(user);

                    // Expire old tokens and save new ones
                    return changeStatusTokensService.expireAllUserTokens(user)
                        .then(changeStatusTokensService.expireAllUserRefreshTokens(user))
                        .then(changeStatusTokensService.saveUserToken(user, jwtToken))
                        .then(changeStatusTokensService.saveUserRefreshToken(user, refreshToken))
                        .thenReturn(ResponseEntity.ok(MessageResponse.builder()
                            .message("User authorization " + user.getEmail() + " is successful")
                            .status(true)
                            .mainResponse(AuthenticationResponse.builder()
                                .accessToken(jwtToken)
                                .refreshToken(refreshToken)
                                .build())
                            .build()));
                });

        }).switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(MessageResponse.builder()
            .message("Incorrect email. User not found...")
            .status(false)
            .build())));
    }

    public Mono<ResponseEntity<MessageResponse>> refreshTokens(ServerHttpRequest request, ServerHttpResponse response) {
        final String authHeader = request.getHeaders().getFirst("Authorization");
        final String jwt = authHeader != null && authHeader.startsWith("Bearer ") ? authHeader.substring(7) : null;

        if (jwt == null) {
            return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(MessageResponse.builder().message("Authorization header is missing")
                    .status(false).build()));
        }

        return accessTokenRepository.findByToken(jwt).flatMap(accessToken -> {
            var userId = accessToken.getUserId();
            return refreshTokenRepository.findValidRefreshTokenByUserId(userId)
                .flatMap(refreshToken -> userRepository.findById(accessToken.getUserId())
                    .flatMap(user -> {
                        if (jwtService.isTokenValid(refreshToken.getToken(), user)) {
                            return expireAndGenerateNewTokens(user, response);
                        } else {
                            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body(MessageResponse.builder()
                                    .message("Refresh token is invalid or expired")
                                    .status(false)
                                    .build()));
                        }
                    }));
        }).switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(MessageResponse.builder().message("Token not found")
                .status(false).build())));
    }

    private Mono<ResponseEntity<MessageResponse>> expireAndGenerateNewTokens(User user, ServerHttpResponse response) {
        var newAccessToken = jwtService.generateToken(user);
        var newRefreshToken = jwtService.generateRefreshToken(user);

        return changeStatusTokensService.expireAllUserTokens(user)
            .then(changeStatusTokensService.expireAllUserRefreshTokens(user))
            .then(changeStatusTokensService.saveUserToken(user, newAccessToken))
            .then(changeStatusTokensService.saveUserRefreshToken(user, newRefreshToken))
            .then(Mono.defer(() -> {
                var messageResponse = MessageResponse.builder()
                    .message("Tokens were refreshed")
                    .status(true)
                    .mainResponse(AuthenticationResponse.builder()
                        .accessToken(newAccessToken)
                        .refreshToken(newRefreshToken)
                        .build())
                    .build();

                try {
                    return response.writeWith(Mono.just(response.bufferFactory()
                            .wrap(new ObjectMapper().writeValueAsBytes(messageResponse))))
                        .thenReturn(ResponseEntity.ok(messageResponse));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }))
            .onErrorResume(IOException.class, e -> Mono.error(new RuntimeException("Error writing response", e)));
    }


}
