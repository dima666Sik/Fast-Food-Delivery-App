package dev.food.fast.server.general.controllers;

import dev.food.fast.server.general.dto.request.ProductReviewRequest;
import dev.food.fast.server.general.service.ProductReviewService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/private/food-reviews")
@RequiredArgsConstructor
public class ProductReviewAuthController {
    private final ProductReviewService productReviewService;

    @PostMapping("/add-review-for-product")
    public ResponseEntity<?> addProductReview(Authentication authentication,
                                              @RequestBody ProductReviewRequest productReviewRequest) {
        return productReviewService.addProductReview(authentication, productReviewRequest);
    }

    @DeleteMapping("/delete-review-to-product")
    public ResponseEntity<?> deleteProductReview(Authentication authentication,
                                              @RequestParam("product_id") Long productId,
                                              @RequestParam("review_id") Long reviewId) {
        return productReviewService.deleteProductReview(authentication, productId, reviewId);
    }
}
