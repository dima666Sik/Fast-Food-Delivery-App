package ua.dev.food.fast.service.service;

import org.springframework.http.server.reactive.ServerHttpRequest;
import reactor.core.publisher.Mono;
import ua.dev.feign.clients.UserDto;
import ua.dev.food.fast.service.domain.dto.request.AuthenticationRequestDto;
import ua.dev.food.fast.service.domain.dto.request.RegisterRequestDto;
import ua.dev.food.fast.service.domain.dto.response.AuthenticationResponseDto;

public interface AuthenticationService {
    Mono<AuthenticationResponseDto> register(RegisterRequestDto request);

    Mono<AuthenticationResponseDto> authenticate(AuthenticationRequestDto request);

    Mono<AuthenticationResponseDto> refreshTokens(ServerHttpRequest request);

    Mono<UserDto> getUserByUserId(Long userId);
}
