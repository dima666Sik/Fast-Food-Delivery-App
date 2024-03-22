package dev.food.fast.server.general.service;

import dev.food.fast.server.auth.models.User;
import dev.food.fast.server.auth.dto.response.MessageResponse;
import dev.food.fast.server.auth.repository.UserRepository;
import dev.food.fast.server.auth.service.JwtService;
import dev.food.fast.server.general.models.product.Product;
import dev.food.fast.server.general.models.product.ProductStatusLikes;
import dev.food.fast.server.general.dto.request.ProductStatusLikedRequest;
import dev.food.fast.server.general.dto.request.ProductStatusRequest;
import dev.food.fast.server.general.repository.ProductStatusLikesRepository;
import dev.food.fast.server.general.repository.ProductsRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductStatusLikesService {
    private final UserRepository userRepository;
    private final ProductsRepository productsRepository;
    private final ProductStatusLikesRepository productLikesRepository;

    public ResponseEntity<?> setStatusOnProduct(
            Authentication authentication,
            ProductStatusRequest statusRequest
    ) {
        try {
            String userEmail = authentication.getName();
            System.out.println("userEmail: "+userEmail);
            var userOptional = userRepository.findByEmail(userEmail);
            if (userOptional.isEmpty()) {
                return ResponseEntity.ok()
                        .body(MessageResponse.builder()
                                .message("User not found...")
                                .status(false)
                                .build());
            }
            User user = userOptional.get();
            System.out.println("statusRequest.getIdProduct(): " + statusRequest.getIdProduct());
            var productOptional = productsRepository.findById(statusRequest.getIdProduct());

            if (productOptional.isEmpty()) {
                return ResponseEntity.ok()
                        .body(MessageResponse.builder()
                                .message("Product not found...")
                                .status(false)
                                .build());
            }
            Product product = productOptional.get();

            var productLikesOptional = productLikesRepository.findByProduct_IdAndUser_Id(statusRequest.getIdProduct(),
                    user.getId());
            if (productLikesOptional.isPresent()) {
                ProductStatusLikes productLikes = productLikesOptional.get();
                productLikes.setStatus(statusRequest.getStatus());
                productLikesRepository.save(productLikes);

                return ResponseEntity.ok("Status updated successfully");
            } else {
                var productLikes = ProductStatusLikes.builder()
                        .status(statusRequest.getStatus())
                        .user(user)
                        .product(product)
                        .build();

                productLikesRepository.save(productLikes);

                return ResponseEntity.ok("Status new added successfully");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok("Status added unsuccessfully");
        }
    }

    public ResponseEntity<?> updateLikeOnProduct(ProductStatusLikedRequest likedRequest) {
        System.out.println("likedRequest.getIdProduct(): " + likedRequest.getIdProduct());
        var productOptional = productsRepository.findById(likedRequest.getIdProduct());

        if (productOptional.isEmpty()) {
            return ResponseEntity.ok()
                    .body(MessageResponse.builder()
                            .message("Product not found...")
                            .status(false)
                            .build());
        }

        Product product = productOptional.get();

        product.setLikes(likedRequest.getLikes());

        productsRepository.save(product);

        return ResponseEntity.ok("Likes added successfully");
    }

    public ResponseEntity<?> getListStatusProducts(Authentication authentication) {

        String userEmail = authentication.getName();

        try {
            var userOptional = userRepository.findByEmail(userEmail);
            if (userOptional.isEmpty()) {
                return ResponseEntity.ok()
                        .body(MessageResponse.builder()
                                .message("User not found...")
                                .status(false)
                                .build());
            }
            User user = userOptional.get();

            var productLikesDTOList = productLikesRepository.findResponseStatusLikedByUser_Id(user.getId());

            if (productLikesDTOList.isEmpty()) {
                return ResponseEntity.ok()
                        .body(MessageResponse.builder()
                                .message("Products not found...")
                                .status(false)
                                .build());
            }

            return ResponseEntity.ok(productLikesDTOList);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok("Status get unsuccessfully");
    }
}
