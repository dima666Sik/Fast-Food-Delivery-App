package dev.food.fast.server.auth.controllers;

import dev.food.fast.server.auth.service.DataUserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class DataUserController {

    private final DataUserService service;

    @GetMapping("/get-access-data")
    public ResponseEntity<?> getAccessData(Authentication authentication
    ) {
        return service.getData(authentication);
    }

}
