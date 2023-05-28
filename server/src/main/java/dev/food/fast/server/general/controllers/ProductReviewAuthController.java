package dev.food.fast.server.general.controllers;

import dev.food.fast.server.general.pojo.ProductReviewRequest;
import dev.food.fast.server.general.service.ProductReviewService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/private/food-reviews")
@RequiredArgsConstructor
public class ProductReviewAuthController {
    private final ProductReviewService productReviewService;

    @PostMapping("/add-review-for-product")
    public ResponseEntity<?> addProductReview(@NonNull HttpServletRequest request,
                                        @RequestBody ProductReviewRequest productReviewRequest) {
        return productReviewService.addProductReview(request, productReviewRequest);
    }

}
