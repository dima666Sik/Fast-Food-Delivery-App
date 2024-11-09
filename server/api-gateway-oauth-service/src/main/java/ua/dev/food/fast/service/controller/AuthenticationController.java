package ua.dev.food.fast.service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ua.dev.food.fast.service.domain.dto.request.AuthenticationRequestDto;
import ua.dev.food.fast.service.domain.dto.request.RegisterRequestDto;
import ua.dev.food.fast.service.domain.dto.response.AuthenticationResponseDto;
import ua.dev.food.fast.service.service.AuthenticationService;

@RestController
@RequestMapping("/api/v2/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public Mono<ResponseEntity<AuthenticationResponseDto>> register(@RequestBody RegisterRequestDto request) {
        return authenticationService.register(request)
            .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<AuthenticationResponseDto>> authenticate(@RequestBody AuthenticationRequestDto request) {
        return authenticationService.authenticate(request).map(ResponseEntity::ok);
    }


    @PostMapping("/refresh-tokens")
    public Mono<ResponseEntity<AuthenticationResponseDto>> refreshTokens(ServerHttpRequest request) {
        return authenticationService.refreshTokens(request)
            .map(apiResponse -> ResponseEntity.status(HttpStatus.OK).body(apiResponse));
    }

}
