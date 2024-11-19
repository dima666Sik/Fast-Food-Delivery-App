package ua.dev.food.fast.service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ua.dev.food.fast.service.domain.dto.request.ProductReviewRequestDto;
import ua.dev.food.fast.service.service.ProductReviewService;

@RestController
@RequestMapping("/api/v2/product/private/food-reviews")
@RequiredArgsConstructor
public class ProductReviewAuthController {
    private final ProductReviewService productReviewService;

    @PostMapping("/add-review-for-product")
    public Mono<ResponseEntity<Void>> addProductReview(@RequestHeader(value = "Authorization") String authHeader,
                                                       @RequestBody ProductReviewRequestDto productReviewRequestDto) {
        return productReviewService.addProductReview(authHeader, productReviewRequestDto)
            .then(Mono.fromCallable(() -> ResponseEntity.status(HttpStatus.CREATED).build()));
    }

    @DeleteMapping("/delete-review-to-product")
    public Mono<ResponseEntity<Void>> deleteProductReview(@RequestHeader(value = "Authorization") String authHeader,
                                                          @RequestParam("product_id") Long productId,
                                                          @RequestParam("product_review_id") Long productReviewId) {
        return productReviewService.deleteProductReview(authHeader, productId, productReviewId)
            .then(Mono.fromCallable(() -> ResponseEntity.ok().build()));
    }
}
