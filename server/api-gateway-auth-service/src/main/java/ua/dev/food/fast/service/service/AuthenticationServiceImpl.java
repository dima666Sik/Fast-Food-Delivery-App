package ua.dev.food.fast.service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.dev.feign.clients.UserDto;
import ua.dev.food.fast.service.domain.dto.request.AuthenticationRequestDto;
import ua.dev.food.fast.service.domain.dto.request.RegisterRequestDto;
import ua.dev.food.fast.service.domain.dto.response.AuthenticationResponseDto;
import ua.dev.food.fast.service.domain.model.Permission;
import ua.dev.food.fast.service.domain.model.Role;
import ua.dev.food.fast.service.domain.model.User;
import ua.dev.food.fast.service.domain.model.UserPermission;
import ua.dev.food.fast.service.exception_handler.exception.AuthorizationTokenException;
import ua.dev.food.fast.service.exception_handler.exception.IncorrectUserDataException;
import ua.dev.food.fast.service.exception_handler.exception.UserAlreadyExistsException;
import ua.dev.food.fast.service.repository.*;
import ua.dev.food.fast.service.util.ConstantMessageExceptions;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final TokensHandlingService changeStatusTokensService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AccessTokenRepository accessTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;
    private final UserPermissionRepository userPermissionRepository;

    @Override
    @Transactional
    public Mono<AuthenticationResponseDto> register(RegisterRequestDto request) {
        return userRepository.findByEmail(request.getEmail())
            .flatMap(user -> Mono.<AuthenticationResponseDto>error(new UserAlreadyExistsException(ConstantMessageExceptions.userAlreadyExists(user.getEmail()))))
            .switchIfEmpty(Mono.defer(() -> {
                var user = User.builder().firstName(request.getFirstName())
                    .lastName(request.getLastName()).email(request.getEmail())
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
                        .thenReturn(AuthenticationResponseDto.builder()
                            .accessToken(jwtToken)
                            .refreshToken(refreshToken)
                            .build());
                });
            }));
    }

    @Override
    @Transactional
    public Mono<AuthenticationResponseDto> authenticate(AuthenticationRequestDto request) {
        return userRepository.findByEmail(request.getEmail()).flatMap(user -> {
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return Mono.error(new IncorrectUserDataException(ConstantMessageExceptions.INCORRECT_PASSWORD));
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
                        .thenReturn(AuthenticationResponseDto.builder()
                            .accessToken(jwtToken)
                            .refreshToken(refreshToken)
                            .build());
                });

        }).switchIfEmpty(Mono.error(new IncorrectUserDataException(ConstantMessageExceptions.INCORRECT_EMAIL)));
    }

    @Override
    @Transactional
    public Mono<AuthenticationResponseDto> refreshTokens(ServerHttpRequest request) {
        final String authHeader = request.getHeaders().getFirst("Authorization");
        final Optional<String> jwt = (authHeader != null && authHeader.startsWith(ConstantMessageExceptions.BEARER_HEADER))
            ? Optional.of(authHeader.substring(7)) : Optional.empty();

        return jwt.map(s -> accessTokenRepository.findByToken(s).flatMap(accessToken -> {
                    var userId = accessToken.getUserId();
                    return refreshTokenRepository.findValidRefreshTokenByUserId(userId)
                        .flatMap(refreshToken -> userRepository.findById(userId)
                            .flatMap(user -> {
                                if (jwtService.isTokenValid(refreshToken.getToken(), user)) {
                                    return expireAndGenerateNewTokens(user);
                                } else {
                                    return Mono.error(new AuthorizationTokenException(ConstantMessageExceptions.INVALID_TOKEN));
                                }
                            }));
                })
                .switchIfEmpty(Mono.error(new AuthorizationTokenException(ConstantMessageExceptions.ACCESS_TOKEN_NOT_FOUND))))
            .orElseGet(() -> Mono.error(new AuthorizationTokenException(ConstantMessageExceptions.AUTHORIZATION_HEADER_IS_EMPTY)));

    }

    @Override
    public Mono<UserDto> getUserByUserId(Long userId) {
        return userRepository.findById(userId)
            .map(user -> UserDto.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build())
            .switchIfEmpty(Mono.error(new IncorrectUserDataException(ConstantMessageExceptions.USER_NOT_FOUND)));
    }

    private Mono<AuthenticationResponseDto> expireAndGenerateNewTokens(User user) {
        var newAccessToken = jwtService.generateToken(user);
        var newRefreshToken = jwtService.generateRefreshToken(user);

        return changeStatusTokensService.expireAllUserTokens(user)
            .then(changeStatusTokensService.expireAllUserRefreshTokens(user))
            .then(changeStatusTokensService.saveUserToken(user, newAccessToken))
            .then(changeStatusTokensService.saveUserRefreshToken(user, newRefreshToken))
            .map(saved -> AuthenticationResponseDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build());
    }


}
