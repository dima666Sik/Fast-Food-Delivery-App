package dev.food.fast.server.general.controllers;

import dev.food.fast.server.auth.repository.AccessTokenRepository;
import dev.food.fast.server.auth.repository.RefreshTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demo-controller")
public class DemoController {

    private AccessTokenRepository tokenRepository;
    private RefreshTokenRepository refreshTokenRepository;

    @GetMapping
    public ResponseEntity<String> sayHello(HttpServletRequest request) {
        return ResponseEntity.ok("Hello from secured endpoint");
    }

}
