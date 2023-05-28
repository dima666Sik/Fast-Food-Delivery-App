package dev.food.fast.server.auth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.food.fast.server.auth.pojo.AuthenticationRequest;
import dev.food.fast.server.auth.pojo.MessageResponse;
import dev.food.fast.server.auth.pojo.RegisterRequest;
import dev.food.fast.server.auth.repository.UserRepository;
import dev.food.fast.server.auth.pojo.AuthenticationResponse;
import dev.food.fast.server.auth.models.Role;
import dev.food.fast.server.auth.models.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final ChangeStatusTokensService changeStatusTokensService;
  private final UserRepository repository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public ResponseEntity<?> register(RegisterRequest request) {
    System.out.println(request);
    if (repository.findByEmail(request.getEmail()).isPresent()) {
      //badRequest()
      return ResponseEntity.ok()
              .body(MessageResponse.builder()
                      .message("User with email " + request.getEmail() + " already exists")
                      .status(false)
                      .build());
    }

    var user = User.builder()
        .firstname(request.getFirstName())
        .lastname(request.getLastName())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(Role.USER)
        .build();
    var savedUser = repository.save(user);
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    changeStatusTokensService.saveUserToken(savedUser, jwtToken);
    changeStatusTokensService.saveUserRefreshToken(user, refreshToken);

    return ResponseEntity.ok().body(AuthenticationResponse.builder()
        .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .messageResponse(new MessageResponse("User registration " + request.getEmail() + " is successful", true))
        .build());
  }

  public ResponseEntity<?> authenticate(AuthenticationRequest request) {
    var userOptional = repository.findByEmail(request.getEmail());
    System.out.println("authenticate+");
    if (userOptional.isEmpty()) {
      return ResponseEntity.ok()
              .body(MessageResponse.builder()
                      .message("Incorrect email. User not found...")
                      .status(false)
                      .build());
    }

    var user = userOptional.get();
    System.out.println("authenticate++");
    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      return ResponseEntity.ok()
              .body(MessageResponse.builder()
                      .message("Incorrect password. User not found...")
                      .status(false)
                      .build());
    }

    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getEmail(),
            request.getPassword()
        )
    );
    System.out.println("user: "+user);
    System.out.println("authenticate+++");
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);

    System.out.println("authenticate++++");
    changeStatusTokensService.revokeAllUserTokens(user);
    changeStatusTokensService.revokeAllUserRefreshTokens(user);

    System.out.println("authenticate+++++");
    changeStatusTokensService.saveUserToken(user, jwtToken);
    changeStatusTokensService.saveUserRefreshToken(user, refreshToken);
    System.out.println("authenticate++++++");
    return ResponseEntity.ok().body(AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .messageResponse(new MessageResponse("User authorization " + request.getEmail() + " is successful", true))
            .build());
  }

}
