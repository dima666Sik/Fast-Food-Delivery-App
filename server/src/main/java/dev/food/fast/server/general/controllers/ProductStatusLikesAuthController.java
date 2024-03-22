package dev.food.fast.server.general.controllers;

import dev.food.fast.server.general.dto.request.ProductStatusLikedRequest;
import dev.food.fast.server.general.dto.request.ProductStatusRequest;
import dev.food.fast.server.general.service.ProductStatusLikesService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/private/product-like")
@RequiredArgsConstructor
public class ProductStatusLikesAuthController {
    private final ProductStatusLikesService productLikesService;

    @PutMapping("/set-like-product")
    public ResponseEntity<?> updateLikeOnProduct(
            @RequestBody ProductStatusLikedRequest likedRequest
    ) {
        return productLikesService.updateLikeOnProduct(likedRequest);
    }

    @PostMapping("/set-status-product")
    public ResponseEntity<?> setStatusProduct(
            Authentication authentication,
            @RequestBody ProductStatusRequest statusRequest
    ) {
        return productLikesService.setStatusOnProduct(authentication, statusRequest);
    }

    @GetMapping("/get-status-products")
    public ResponseEntity<?> getListStatusProducts(
            Authentication authentication
    ) {
        return productLikesService.getListStatusProducts(authentication);
    }
}
