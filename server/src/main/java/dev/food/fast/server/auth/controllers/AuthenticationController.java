package dev.food.fast.server.auth.controllers;

import dev.food.fast.server.auth.dto.request.AuthenticationRequest;
import dev.food.fast.server.auth.dto.request.RegisterRequest;
import dev.food.fast.server.auth.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService service;

  @PostMapping("/registration")
  public ResponseEntity<?> register(
      @RequestBody RegisterRequest request
  ) {
    return service.register(request);
  }
  @PostMapping("/login")
  public ResponseEntity<?> authenticate(
      @RequestBody AuthenticationRequest request
  ) {
    System.out.println("service.authenticate");
    return service.authenticate(request);
  }

  @PostMapping("/refresh-tokens")
  public ResponseEntity<?> refreshTokens(
          @NonNull HttpServletRequest request,
          @NonNull HttpServletResponse response
  ) {
    System.out.println("service.refresh-tokens");
    return service.refreshTokens(request, response);
  }

}
