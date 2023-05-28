package dev.food.fast.server.auth.controllers;

import dev.food.fast.server.auth.pojo.AuthenticationRequest;
import dev.food.fast.server.auth.pojo.RegisterRequest;
import dev.food.fast.server.auth.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    System.out.println("service.authenticate++++");
    return service.authenticate(request);
  }

}
