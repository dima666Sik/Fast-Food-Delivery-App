package dev.food.fast.server.general.controllers;

import dev.food.fast.server.general.pojo.ProductStatusLikedRequest;
import dev.food.fast.server.general.pojo.ProductStatusRequest;
import dev.food.fast.server.general.service.ProductStatusLikesService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/private/product-like")
@RequiredArgsConstructor
public class ProductStatusLikesAuthController {
    private final ProductStatusLikesService productLikesService;

    @PostMapping("/set-like-product")
    public ResponseEntity<?> updateLikeOnProduct(
            @RequestBody ProductStatusLikedRequest likedRequest
    ) {
        return productLikesService.updateLikeOnProduct(likedRequest);
    }

    @PostMapping("/set-status-product")
    public ResponseEntity<?> setStatusProduct(
            @NonNull HttpServletRequest request,
            @RequestBody ProductStatusRequest statusRequest
    ) {
        return productLikesService.setStatusOnProduct(request, statusRequest);
    }

//    @GetMapping("/get-like-product")
//    public ResponseEntity<?> getLikeOnProduct(
//            @RequestParam("product_id") Integer productId
//    ) {
//        return productLikesService.getLikeOnProduct(
//                productId
//        );
//    }
//
//    @GetMapping("/get-status-product")
//    public ResponseEntity<?> getStatusProduct(
//            @NonNull HttpServletRequest request,
//            @RequestParam("product_id") Integer productId
//    ) {
//        return productLikesService.getStatusProduct(request, productId);
//    }

    @GetMapping("/get-status-products")
    public ResponseEntity<?> getStatusProducts(
            @NonNull HttpServletRequest request
    ) {
        return productLikesService.getListStatusProducts(request);
    }
}
