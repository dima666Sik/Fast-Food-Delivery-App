package ua.dev.food.fast.service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ua.dev.feign.clients.UserDto;
import ua.dev.food.fast.service.domain.dto.request.AuthenticationRequestDto;
import ua.dev.food.fast.service.domain.dto.request.RegisterRequestDto;
import ua.dev.food.fast.service.domain.dto.response.AuthenticationResponseDto;
import ua.dev.food.fast.service.service.AuthenticationService;

/***
 * Controller class for handling authentication-related operations.
 */
@RestController
@RequestMapping("/api/v2/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    /**
     * Retrieves a user by their user ID.
     *
     * @param id The unique identifier of the user to retrieve.
     * @return A Mono that emits a ResponseEntity containing the UserDto if found, or an appropriate error response.
     */
    @GetMapping("/users/{id}")
    public Mono<ResponseEntity<UserDto>> getUserByUserId(@PathVariable Long id) {
        return authenticationService.getUserByUserId(id)
            .map(response -> ResponseEntity.status(HttpStatus.OK).body(response));
    }

    /**
     * Registers a new user.
     *
     * @param request The RegisterRequestDto containing the user's registration information.
     * @return A Mono that emits a ResponseEntity containing the AuthenticationResponseDto upon successful registration.
     */
    @PostMapping("/register")
    public Mono<ResponseEntity<AuthenticationResponseDto>> registerUser(@RequestBody RegisterRequestDto request) {
        return authenticationService.registerUser(request)
            .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
    }

    /**
     * Registers a new admin user.
     *
     * @param request The RegisterRequestDto containing the admin's registration information.
     * @return A Mono that emits a ResponseEntity containing the AuthenticationResponseDto upon successful registration.
     */
    @PostMapping("/admin/register")
    public Mono<ResponseEntity<AuthenticationResponseDto>> registerAdmin(@RequestBody RegisterRequestDto request) {
        return authenticationService.registerAdmin(request)
            .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
    }

    /**
     * Registers a new courier user.
     *
     * @param request The RegisterRequestDto containing the courier's registration information.
     * @return A Mono that emits a ResponseEntity containing the AuthenticationResponseDto upon successful registration.
     */
    @PostMapping("/courier/register")
    public Mono<ResponseEntity<AuthenticationResponseDto>> registerCourier(@RequestBody RegisterRequestDto request) {
        return authenticationService.registerCourier(request)
            .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
    }

    /**
     * Authenticates a user.
     *
     * @param request The AuthenticationRequestDto containing the user's login credentials.
     * @return A Mono that emits a ResponseEntity containing the AuthenticationResponseDto upon successful authentication.
     */
    @PostMapping("/login")
    public Mono<ResponseEntity<AuthenticationResponseDto>> authenticate(@RequestBody AuthenticationRequestDto request) {
        return authenticationService.authenticate(request).map(ResponseEntity::ok);
    }

    /**
     * Refreshes the authentication tokens for a user.
     *
     * @param request The ServerHttpRequest containing the current authentication information.
     * @return A Mono that emits a ResponseEntity containing the AuthenticationResponseDto with refreshed tokens.
     */
    @PostMapping("/refresh-tokens")
    public Mono<ResponseEntity<AuthenticationResponseDto>> refreshTokens(ServerHttpRequest request) {
        return authenticationService.refreshTokens(request)
            .map(apiResponse -> ResponseEntity.status(HttpStatus.OK).body(apiResponse));
    }
}