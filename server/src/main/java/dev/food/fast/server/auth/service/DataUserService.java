package dev.food.fast.server.auth.service;

import dev.food.fast.server.auth.dto.response.DataUserResponse;
import dev.food.fast.server.auth.models.User;
import dev.food.fast.server.auth.dto.response.MessageResponse;
import dev.food.fast.server.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DataUserService {
    private final UserRepository userRepository;

    public ResponseEntity<?> getData(Authentication authentication) {

        String userEmail = authentication.getName();

        try {
            var userOptional = userRepository.findByEmail(userEmail);
            if (userOptional.isEmpty()) {
                return ResponseEntity.ok()
                        .body(MessageResponse.builder()
                                .message("User not found...")
                                .status(false)
                                .build());
            }
            User user = userOptional.get();

            DataUserResponse userDto = DataUserResponse.builder()
                    .firstname(user.getFirstname())
                    .lastname(user.getLastname())
                    .email(user.getEmail())
                    .build();
            return ResponseEntity.ok(userDto);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(MessageResponse.builder()
                            .message("Failed to get user: " + e.getMessage())
                            .status(false)
                            .build());
        }
    }
}
