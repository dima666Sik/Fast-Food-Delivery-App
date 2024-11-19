package ua.dev.food.fast.service.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ua.dev.feign.clients.UserDto;
import ua.dev.food.fast.service.domain.dto.request.AuthenticationRequestDto;
import ua.dev.food.fast.service.domain.dto.request.RegisterRequestDto;
import ua.dev.food.fast.service.domain.dto.response.AuthenticationResponseDto;
import ua.dev.food.fast.service.domain.model.Permission;
import ua.dev.food.fast.service.domain.model.Role;
import ua.dev.food.fast.service.domain.model.User;
import ua.dev.food.fast.service.domain.model.UserPermission;
import ua.dev.food.fast.service.exception_handler.exception.IncorrectUserDataException;
import ua.dev.food.fast.service.exception_handler.exception.UserAlreadyExistsException;
import ua.dev.food.fast.service.repository.PermissionRepository;
import ua.dev.food.fast.service.repository.UserPermissionRepository;
import ua.dev.food.fast.service.repository.UserRepository;
import ua.dev.food.fast.service.util.ConstantMessageExceptions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class AuthenticationServiceImplTest {
    @Mock
    private TokensHandlingService changeStatusTokensService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PermissionRepository permissionRepository;
    @Mock
    private UserPermissionRepository userPermissionRepository;
    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    private RegisterRequestDto registerRequestDto;
    private AuthenticationRequestDto authenticationRequestDto;
    private User user;

    @BeforeEach
    void setUp() {
        registerRequestDto = RegisterRequestDto.builder().email("john.doe@example.com").firstName("John")
            .lastName("Doe").password("password123").build();
        authenticationRequestDto = AuthenticationRequestDto.builder().email("john.doe@example.com")
            .password("password123").build();

        user = User.builder().id(1L).email("john.doe@example.com").firstName("John").lastName("Doe")
            .password("encodedPassword").permissions(List.of(Permission.builder().role(Role.USER).build())).build();
    }

    @Test
    void testRegisterWhenUserAlreadyExists() {
        when(userRepository.findByEmail(registerRequestDto.getEmail())).thenReturn(Mono.just(user));

        Mono<AuthenticationResponseDto> result = authenticationService.register(registerRequestDto);

        StepVerifier.create(result)
            .expectErrorMatches(throwable -> throwable instanceof UserAlreadyExistsException && throwable.getMessage()
                .equals("User with email " + user.getEmail() + " already exists")).verify();

        verify(userRepository).findByEmail(registerRequestDto.getEmail());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void testRegisterWhenUserDoesNotExist() {
        when(userRepository.findByEmail(registerRequestDto.getEmail())).thenReturn(Mono.empty());
        when(passwordEncoder.encode(registerRequestDto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(Mono.just(user));
        when(permissionRepository.findPermissionByRole(Role.USER.name())).thenReturn(Mono.just(Permission.builder()
            .role(Role.USER).build()));
        when(userPermissionRepository.save(any(UserPermission.class))).thenReturn(Mono.just(new UserPermission()));
        when(jwtService.generateToken(user)).thenReturn("accessToken");
        when(jwtService.generateRefreshToken(user)).thenReturn("refreshToken");
        when(changeStatusTokensService.saveUserToken(user, "accessToken")).thenReturn(Mono.empty());
        when(changeStatusTokensService.saveUserRefreshToken(user, "refreshToken")).thenReturn(Mono.empty());

        Mono<AuthenticationResponseDto> result = authenticationService.register(registerRequestDto);

        StepVerifier.create(result).assertNext(response -> {
            assertEquals("accessToken", response.getAccessToken());
            assertEquals("refreshToken", response.getRefreshToken());
        }).verifyComplete();
    }

    @Test
    void testAuthenticateWithCorrectPassword() {
        when(userRepository.findByEmail(authenticationRequestDto.getEmail())).thenReturn(Mono.just(user));
        when(passwordEncoder.matches(authenticationRequestDto.getPassword(), user.getPassword())).thenReturn(true);
        when(userPermissionRepository.findAllByUserId(user.getId())).thenReturn(Flux.just(UserPermission.builder()
            .userId(user.getId()).permissionId(1L).build()));
        when(permissionRepository.findById(1L)).thenReturn(Mono.just(Permission.builder().role(Role.USER).build()));
        when(jwtService.generateToken(user)).thenReturn("accessToken");
        when(jwtService.generateRefreshToken(user)).thenReturn("refreshToken");
        when(changeStatusTokensService.expireAllUserTokens(user)).thenReturn(Mono.empty());
        when(changeStatusTokensService.expireAllUserRefreshTokens(user)).thenReturn(Mono.empty());
        when(changeStatusTokensService.saveUserToken(user, "accessToken")).thenReturn(Mono.empty());
        when(changeStatusTokensService.saveUserRefreshToken(user, "refreshToken")).thenReturn(Mono.empty());

        Mono<AuthenticationResponseDto> result = authenticationService.authenticate(authenticationRequestDto);

        StepVerifier.create(result).assertNext(response -> {
            assertEquals("accessToken", response.getAccessToken());
            assertEquals("refreshToken", response.getRefreshToken());
        }).verifyComplete();


    }

    @Test
    void testAuthenticateWithIncorrectPassword() {
        when(userRepository.findByEmail(authenticationRequestDto.getEmail())).thenReturn(Mono.just(user));
        when(passwordEncoder.matches(authenticationRequestDto.getPassword(), user.getPassword())).thenReturn(false);

        Mono<AuthenticationResponseDto> result = authenticationService.authenticate(authenticationRequestDto);

        StepVerifier.create(result)
            .expectErrorMatches(throwable -> throwable instanceof IncorrectUserDataException && throwable.getMessage()
                .equals(ConstantMessageExceptions.INCORRECT_PASSWORD)).verify();
    }

    @Test
    void testAuthenticateWithNonExistentUser() {
        when(userRepository.findByEmail(authenticationRequestDto.getEmail())).thenReturn(Mono.empty());

        Mono<AuthenticationResponseDto> result = authenticationService.authenticate(authenticationRequestDto);

        StepVerifier.create(result)
            .expectErrorMatches(throwable -> throwable instanceof IncorrectUserDataException && throwable.getMessage()
                .equals(ConstantMessageExceptions.INCORRECT_EMAIL)).verify();
    }

    @Test
    void testGetUserByIdByUnExistUserId() {
        when(userRepository.findById(user.getId())).thenReturn(Mono.empty());

        Mono<UserDto> result = authenticationService.getUserByUserId(user.getId());

        StepVerifier.create(result)
            .expectErrorMatches(throwable -> throwable instanceof IncorrectUserDataException && throwable.getMessage()
                .equals(ConstantMessageExceptions.USER_NOT_FOUND)).verify();
    }

    @Test
    void testGetUserByIdSuccessfully() {
        when(userRepository.findById(user.getId()))
            .thenReturn(Mono.just(user));

        Mono<UserDto> result = authenticationService.getUserByUserId(user.getId());

        StepVerifier.create(result).assertNext(Assertions::assertNotNull).verifyComplete();
    }
}
