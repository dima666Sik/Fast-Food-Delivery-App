package ua.dev.food.fast.service.controller;

import ua.dev.food.fast.service.domain.dto.request.AuthenticationRequest;
import ua.dev.food.fast.service.domain.dto.request.RegisterRequest;
import ua.dev.food.fast.service.domain.dto.response.MessageResponse;
import ua.dev.food.fast.service.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;

@RestController
@RequestMapping("/api/v2/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public Mono<ResponseEntity<MessageResponse>> register(@RequestBody RegisterRequest request) {
        return service.register(request);
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<MessageResponse>> authenticate(@RequestBody AuthenticationRequest request) {
        return service.authenticate(request);
    }


    @PostMapping("/refresh-tokens")
    public Mono<ResponseEntity<MessageResponse>> refreshTokens(ServerHttpRequest request) {
        return service.refreshTokens(request);
    }

}
