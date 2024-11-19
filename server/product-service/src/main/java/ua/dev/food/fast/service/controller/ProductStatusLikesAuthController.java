package ua.dev.food.fast.service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.dev.food.fast.service.domain.dto.request.ProductStatusLikedRequestDto;
import ua.dev.food.fast.service.domain.dto.request.ProductStatusRequestDto;
import ua.dev.food.fast.service.domain.dto.response.ProductStatusLikesResponseDto;
import ua.dev.food.fast.service.service.ProductStatusLikesService;
import ua.dev.food.fast.service.util.MessageConstants;

import java.util.List;

@RestController
@RequestMapping("/api/v2/product/private/product-like")
@RequiredArgsConstructor
public class ProductStatusLikesAuthController {
    private final ProductStatusLikesService productLikesService;

    @PutMapping("/set-likes-product")
    public Mono<ResponseEntity<String>> updateLikeOnProduct(
        @RequestBody ProductStatusLikedRequestDto likedRequest
    ) {
        return productLikesService.updateLikeOnProduct(likedRequest)
            .then(Mono.fromCallable(() -> ResponseEntity.ok(MessageConstants.PRODUCT_LIKES_SET_SUCCESSFULLY)));
    }

    @PutMapping("/set-status-like-product")
    public Mono<ResponseEntity<String>> setStatusProduct(
        @RequestHeader(value = "Authorization") String authHeader,
        @RequestBody ProductStatusRequestDto statusRequest
    ) {
        return productLikesService.setStatusOnProduct(authHeader, statusRequest)
            .then(Mono.fromCallable(() -> ResponseEntity.ok(MessageConstants.PRODUCT_STATUS_SET_SUCCESSFULLY)));
    }

    @GetMapping("/get-status-products")
    public Mono<ResponseEntity<Flux<ProductStatusLikesResponseDto>>> getListStatusLikesProducts(
        @RequestHeader("Authorization") String authHeader) {
        return Mono.just(ResponseEntity.ok(productLikesService.getListStatusLikesProducts(authHeader)));
    }
}
