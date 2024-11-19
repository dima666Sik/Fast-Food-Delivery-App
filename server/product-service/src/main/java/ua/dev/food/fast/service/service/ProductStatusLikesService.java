package ua.dev.food.fast.service.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.dev.food.fast.service.domain.dto.request.ProductStatusLikedRequestDto;
import ua.dev.food.fast.service.domain.dto.request.ProductStatusRequestDto;
import ua.dev.food.fast.service.domain.dto.response.ProductStatusLikesResponseDto;
import ua.dev.food.fast.service.domain.model.product.Product;
import ua.dev.food.fast.service.domain.model.product.ProductStatusLikes;
import ua.dev.food.fast.service.exception_handler.exception.ResourceNotFoundException;
import ua.dev.food.fast.service.repository.ProductStatusLikesRepository;
import ua.dev.food.fast.service.repository.ProductRepository;
import ua.dev.food.fast.service.util.MessageConstants;
import ua.dev.jwt.service.JwtDecodeService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductStatusLikesService {
    private final JwtDecodeService jwtDecodeService;
    private final ProductRepository productRepository;
    private final ProductStatusLikesRepository productLikesRepository;

    @Transactional
    public Mono<Void> setStatusOnProduct(String authHeader, ProductStatusRequestDto statusRequest) {
        return Mono.defer(() -> {
            Long userId = jwtDecodeService.extractUserDataFromBearerHeader(authHeader);
            return productRepository.findById(statusRequest.idProduct())
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(MessageConstants.PRODUCT_NOT_FOUND)))
                .flatMap(product -> productLikesRepository.findByProductIdAndUserId(statusRequest.idProduct(), userId)
                    .switchIfEmpty(Mono.defer(() -> {
                        // If no existing record is found, create a new one
                        ProductStatusLikes newStatus = ProductStatusLikes.builder().status(statusRequest.status())
                            .userId(userId).productId(product.getId()).build();
                        return productLikesRepository.save(newStatus);
                    })).flatMap(existingStatus -> {
                        // If a record exists, update the status
                        existingStatus.setStatus(statusRequest.status());
                        return productLikesRepository.save(existingStatus);
                    })).then(); // Convert the result to Mono<Void>
        });
    }

    @Transactional
    public Mono<Void> updateLikeOnProduct(ProductStatusLikedRequestDto likedRequest) {
        return productRepository.findById(likedRequest.idProduct())
            .switchIfEmpty(Mono.error(new ResourceNotFoundException(MessageConstants.PRODUCT_NOT_FOUND)))
            .flatMap(product -> {
                // Update the likes count on the product
                product.setLikes(likedRequest.likes());
                return productRepository.save(product);
            }).then();
    }

    @Transactional
    public Flux<ProductStatusLikesResponseDto> getListStatusLikesProducts(String authHeader) {
        return Flux.defer(() -> {
            Long userId = jwtDecodeService.extractUserDataFromBearerHeader(authHeader);
            return productLikesRepository.findByUserId(userId)
                .switchIfEmpty(Flux.error(new ResourceNotFoundException(MessageConstants.NOT_FOUND_LIKES_FROM_USER)))
                .flatMap(productStatusLikes -> productRepository.findById(productStatusLikes.getProductId())
                    .map(product -> ProductStatusLikesResponseDto.builder().id(productStatusLikes.getId())
                        .likes(product.getLikes()).status(productStatusLikes.getStatus()).build()));
        });
    }
}
