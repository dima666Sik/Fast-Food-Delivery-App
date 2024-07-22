package dev.food.fast.server.general.service;

import dev.food.fast.server.auth.models.User;
import dev.food.fast.server.auth.dto.response.MessageResponse;
import dev.food.fast.server.auth.repository.UserRepository;
import dev.food.fast.server.general.models.product.Product;
import dev.food.fast.server.general.models.product.ProductReview;
import dev.food.fast.server.general.dto.request.ProductReviewRequest;
import dev.food.fast.server.general.repository.ProductReviewRepository;
import dev.food.fast.server.general.repository.ProductsRepository;
import lombok.RequiredArgsConstructor;
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

    public ResponseEntity<?> deleteProductReview(Authentication authentication, Long productId, Long reviewId) {
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

            var productOptional = productsRepository.findById(productId);
            System.out.println("---------------------------------------+"+ productId + " "+ reviewId);
            if (productOptional.isEmpty()) {
                return ResponseEntity.ok()
                        .body(MessageResponse.builder()
                                .message("Product not found...")
                                .status(false)
                                .build());
            }
            Product product = productOptional.get();
            System.out.println("---------------------------------------++");
            // Проверяем, является ли пользователь автором отзыва
            var isReviewAuthor = productReviewRepository.findByIdAndProductIdAndUserId(reviewId,product.getId(),user.getId());
            System.out.println("---------------------------------------+++");
            if (isReviewAuthor.isEmpty()) {
                return ResponseEntity.ok()
                        .body(MessageResponse.builder()
                                .message("Not found review!")
                                .status(false)
                                .build());
            }

            System.out.println("---------------------------------------+++++");
            productReviewRepository.delete(isReviewAuthor.get());

            return ResponseEntity.ok("Product review was deleted successfully");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok("Product review was add unsuccessfully");
        }
    }
}
