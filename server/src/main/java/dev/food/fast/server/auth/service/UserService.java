package dev.food.fast.server.auth.service;

import dev.food.fast.server.auth.dto.UserDTO;
import dev.food.fast.server.auth.models.User;
import dev.food.fast.server.auth.pojo.MessageResponse;
import dev.food.fast.server.auth.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final JwtService jwtService;
    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<?> getData(@NonNull HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");

        final String jwt = authHeader.substring(7);

        final String userEmail;

        try {
            userEmail = jwtService.extractUsername(jwt);
            var userOptional = userRepository.findByEmail(userEmail);
            if (userOptional.isEmpty()) {
                return ResponseEntity.ok()
                        .body(MessageResponse.builder()
                                .message("User not found...")
                                .status(false)
                                .build());
            }
            User user = userOptional.get();

            UserDTO userDto = UserDTO.builder()
                    .firstname(user.getFirstname())
                    .lastname(user.getLastname())
                    .email(user.getEmail())
                    .build();

            return ResponseEntity.ok(userDto);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok("Get User is unsuccessfully");
        }
    }
}
