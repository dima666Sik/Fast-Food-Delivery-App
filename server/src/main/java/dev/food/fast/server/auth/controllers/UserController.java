package dev.food.fast.server.auth.controllers;

import dev.food.fast.server.auth.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin(allowedHeaders = {"Authorization", "X-Refresh-Token"},exposedHeaders = {"Authorization", "X-Refresh-Token"})
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping("/get-access-data")
    public ResponseEntity<?> getAccessData(@NonNull HttpServletRequest request
    ) {
        return service.getData(request);
    }

}
