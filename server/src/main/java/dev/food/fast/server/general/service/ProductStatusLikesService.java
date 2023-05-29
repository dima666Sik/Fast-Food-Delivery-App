package dev.food.fast.server.general.service;

import dev.food.fast.server.auth.models.User;
import dev.food.fast.server.auth.pojo.MessageResponse;
import dev.food.fast.server.auth.repository.UserRepository;
import dev.food.fast.server.auth.service.JwtService;
import dev.food.fast.server.general.models.product.Product;
import dev.food.fast.server.general.models.product.ProductStatusLikes;
import dev.food.fast.server.general.pojo.ProductStatusLikedRequest;
import dev.food.fast.server.general.pojo.ProductStatusRequest;
import dev.food.fast.server.general.repository.ProductStatusLikesRepository;
import dev.food.fast.server.general.repository.ProductsRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductStatusLikesService {
    private final JwtService jwtService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductsRepository productsRepository;
    @Autowired
    private ProductStatusLikesRepository productLikesRepository;

    public ResponseEntity<?> setStatusOnProduct(
            @NonNull HttpServletRequest request,
            ProductStatusRequest statusRequest
    ) {
        final String authHeader = request.getHeader("Authorization");

        final String jwt = authHeader.substring(7);

        final String userEmail;

        try {
            userEmail = jwtService.extractUsername(jwt);
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

//    public ResponseEntity<?> getLikeOnProduct(
//                 Integer productId
//    ) {
//        var productOptional = productsRepository.findById(
//                productId
//        );
//
//        if (productOptional.isEmpty()) {
//            return ResponseEntity.ok()
//                    .body(MessageResponse.builder()
//                            .message("Product not found...")
//                            .status(false)
//                            .build());
//        }
//
//        Product product = productOptional.get();
//
//        return ResponseEntity.ok(product.getLikes());
//    }

//    public ResponseEntity<?> getStatusProduct(HttpServletRequest request, Integer productId) {
//        final String authHeader = request.getHeader("Authorization");
//        System.out.println("----getStatusProduct: "+authHeader);
//        final String jwt = authHeader.substring(7);
//
//        final String userEmail;
//
//        try {
//            userEmail = jwtService.extractUsername(jwt);
//            var userOptional = userRepository.findByEmail(userEmail);
//            if (userOptional.isEmpty()) {
//                return ResponseEntity.ok()
//                        .body(MessageResponse.builder()
//                                .message("User not found...")
//                                .status(false)
//                                .build());
//            }
//            User user = userOptional.get();
//
//            var productOptional = productsRepository.findById(productId);
//
//            if (productOptional.isEmpty()) {
//                return ResponseEntity.ok()
//                        .body(MessageResponse.builder()
//                                .message("Product not found...")
//                                .status(false)
//                                .build());
//            }
//            Product product = productOptional.get();
//
//            var productLikesOptional = productLikesRepository
//                    .findByProduct_IdAndUser_Id(productId, user.getId());
//
//            if (productLikesOptional.isPresent()) {
//                ProductLikes productLikes = productLikesOptional.get();
//                return ResponseEntity.ok(productLikes.getStatus());
//            } else {
//                return ResponseEntity.ok("Status was not found");
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return ResponseEntity.ok("Status get unsuccessfully");
//    }

    public ResponseEntity<?> getListStatusProducts(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt = authHeader.substring(7);

        final String userEmail;

        try {
            userEmail = jwtService.extractUsername(jwt);
            var userOptional = userRepository.findByEmail(userEmail);
            if (userOptional.isEmpty()) {
                return ResponseEntity.ok()
                        .body(MessageResponse.builder()
                                .message("User not found...")
                                .status(false)
                                .build());
            }
            User user = userOptional.get();

            var productLikesDTOList = productLikesRepository.findDTOByUser_Id(user.getId());

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
