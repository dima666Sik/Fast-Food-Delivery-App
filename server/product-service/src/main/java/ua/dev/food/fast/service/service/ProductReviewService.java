package ua.dev.food.fast.service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.dev.feign.clients.UserFeignClient;
import ua.dev.food.fast.service.domain.dto.request.ProductReviewRequestDto;
import ua.dev.food.fast.service.domain.dto.response.ProductReviewResponseDto;
import ua.dev.food.fast.service.domain.model.product.ProductReview;
import ua.dev.food.fast.service.exception_handler.exception.ResourceNotFoundException;
import ua.dev.food.fast.service.repository.ProductReviewRepository;
import ua.dev.food.fast.service.repository.ProductRepository;
import ua.dev.food.fast.service.util.MessageConstants;
import ua.dev.jwt.service.JwtDecodeService;

@Service
@RequiredArgsConstructor
public class ProductReviewService {
    private final ProductRepository productRepository;
    private final ProductReviewRepository productReviewRepository;
    private final JwtDecodeService jwtDecodeService;
    private final UserFeignClient userFeignClient;

    @Transactional
    public Mono<Void> addProductReview(String authHeader, ProductReviewRequestDto productReviewRequestDto) {
        Long userId = jwtDecodeService.extractUserDataFromBearerHeader(authHeader);

        return productRepository.findById(productReviewRequestDto.idProduct())
            .switchIfEmpty(Mono.error(new ResourceNotFoundException(MessageConstants.PRODUCT_NOT_FOUND)))
            .map(product -> ProductReview.builder()
                .review(productReviewRequestDto.review())
                .productId(product.getId())
                .userId(userId)
                .build())
            .flatMap(productReviewRepository::save)
            .then();
    }

    @Transactional
    public Flux<ProductReviewResponseDto> getAllProductReview(Long productId) {
        return productReviewRepository.findByProductIdOrderByProductIdAsc(productId)
            .switchIfEmpty(Mono.error(new ResourceNotFoundException(MessageConstants.PRODUCT_REVIEW_NOT_FOUND)))
            .flatMap(productReview -> userFeignClient.getUserById(productReview.getUserId())
                .map(userDto -> ProductReviewResponseDto.builder()
                    .id(productReview.getId())
                    .productId(productId)
                    .review(productReview.getReview())
                    .userId(productReview.getUserId())
                    .emailReviewer(userDto.email())
                    .lastNameReviewer(userDto.lastName())
                    .firstNameReviewer(userDto.firstName())
                    .build()));
    }


    @Transactional
    public Mono<Void> deleteProductReview(String authHeader, Long productId, Long productReviewId) {
        Long userId = jwtDecodeService.extractUserDataFromBearerHeader(authHeader);

        return productRepository.findById(productId)
            .switchIfEmpty(Mono.error(new ResourceNotFoundException(MessageConstants.PRODUCT_NOT_FOUND)))
            .flatMap(product -> productReviewRepository.findByIdAndProductIdAndUserId(productReviewId, product.getId(), userId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(MessageConstants.PRODUCT_REVIEW_NOT_FOUND)))
                .flatMap(productReviewRepository::delete));
    }

    @Transactional
    public Mono<Void> editProductReview(String authHeader, Long productId, Long productReviewId, String productReviewMsg) {
        Long userId = jwtDecodeService.extractUserDataFromBearerHeader(authHeader);

        return productRepository.findById(productId)
            .switchIfEmpty(Mono.error(new ResourceNotFoundException(MessageConstants.PRODUCT_NOT_FOUND)))
            .flatMap(product ->
                productReviewRepository.findByIdAndProductIdAndUserId(productReviewId, product.getId(), userId)
                    .flatMap(productReview -> {
                        productReview.setReview(productReviewMsg);
                        return productReviewRepository.save(productReview);
                    })
                    .switchIfEmpty(Mono.error(new ResourceNotFoundException(MessageConstants.PRODUCT_REVIEW_NOT_FOUND)))
            )
            .then();
    }

}
