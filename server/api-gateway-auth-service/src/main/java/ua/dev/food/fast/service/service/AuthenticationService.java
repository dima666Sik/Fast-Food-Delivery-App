package ua.dev.food.fast.service.service;

import org.springframework.http.server.reactive.ServerHttpRequest;
import reactor.core.publisher.Mono;
import ua.dev.feign.clients.UserDto;
import ua.dev.food.fast.service.domain.dto.request.AuthenticationRequestDto;
import ua.dev.food.fast.service.domain.dto.request.RegisterRequestDto;
import ua.dev.food.fast.service.domain.dto.response.AuthenticationResponseDto;

/***
 * Service interface for handling authentication-related operations.
 */
public interface AuthenticationService {
    /**
     * Registers a new user in the system.
     *
     * @param request The registration request containing user details.
     * @return A Mono emitting the authentication response with tokens upon successful registration.
     */
    Mono<AuthenticationResponseDto> registerUser(RegisterRequestDto request);

    /**
     * Authenticates a user based on provided credentials.
     *
     * @param request The authentication request containing user credentials.
     * @return A Mono emitting the authentication response with tokens upon successful authentication.
     */
    Mono<AuthenticationResponseDto> authenticate(AuthenticationRequestDto request);

    /**
     * Refreshes the authentication tokens for a user.
     *
     * @param request The server HTTP request containing the refresh token.
     * @return A Mono emitting the authentication response with new tokens.
     */
    Mono<AuthenticationResponseDto> refreshTokens(ServerHttpRequest request);

    /**
     * Retrieves user information by user ID.
     *
     * @param userId The unique identifier of the user.
     * @return A Mono emitting the user data transfer object.
     */
    Mono<UserDto> getUserByUserId(Long userId);

    /**
     * Registers a new admin user in the system.
     *
     * @param request The registration request containing admin user details.
     * @return A Mono emitting the authentication response with tokens upon successful registration.
     */
    Mono<AuthenticationResponseDto> registerAdmin(RegisterRequestDto request);

    /**
     * Registers a new courier user in the system.
     *
     * @param request The registration request containing courier user details.
     * @return A Mono emitting the authentication response with tokens upon successful registration.
     */
    Mono<AuthenticationResponseDto> registerCourier(RegisterRequestDto request);
}
