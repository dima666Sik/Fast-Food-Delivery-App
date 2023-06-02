package dev.food.fast.server.general.service;

import dev.food.fast.server.auth.models.User;
import dev.food.fast.server.auth.dto.response.MessageResponse;
import dev.food.fast.server.auth.repository.UserRepository;
import dev.food.fast.server.auth.service.JwtService;
import dev.food.fast.server.general.models.product.Product;
import dev.food.fast.server.general.models.product.ProductReview;
import dev.food.fast.server.general.dto.request.ProductReviewRequest;
import dev.food.fast.server.general.repository.ProductReviewRepository;
import dev.food.fast.server.general.repository.ProductsRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductReviewService {
    private final UserRepository userRepository;
    private final ProductsRepository productsRepository;
    private final ProductReviewRepository productReviewRepository;

    public ResponseEntity<?> addProductReview(Authentication authentication, ProductReviewRequest productReviewRequest) {
        try {
            String userEmail = authentication.getName();
            var userOptional = userRepository.findByEmail(userEmail);
            if (userOptional.isEmpty()) {
                return ResponseEntity.ok()
                        .body(MessageResponse.builder()
                                .message("User not found...")
                                .status(false)
                                .build());
            }
            User user = userOptional.get();

            var productOptional = productsRepository.findById(productReviewRequest.getIdProduct());

            if (productOptional.isEmpty()) {
                return ResponseEntity.ok()
                        .body(MessageResponse.builder()
                                .message("Product not found...")
                                .status(false)
                                .build());
            }
            Product product = productOptional.get();

            var productReview = ProductReview.builder()
                    .review(productReviewRequest.getReview())
                    .product(product)
                    .user(user)
                    .build();
            productReviewRepository.save(productReview);

            return ResponseEntity.ok("Product review was add successfully");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok("Product review was add unsuccessfully");
        }
    }

    public ResponseEntity<?> getAllProductReview(Long productId) {
        var productReviewResponsesList = productReviewRepository.findProductReviewByProduct_Id(productId);

        if (productReviewResponsesList.isEmpty()) {
            return ResponseEntity.ok()
                    .body(MessageResponse.builder()
                            .message("Products not found...")
                            .status(false)
                            .build());
        }

        return ResponseEntity.ok(productReviewResponsesList);
    }
}
