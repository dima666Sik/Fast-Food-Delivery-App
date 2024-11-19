package ua.dev.food.fast.service.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ua.dev.feign.clients.UserDto;
import ua.dev.feign.clients.UserFeignClient;
import ua.dev.food.fast.service.domain.dto.request.ProductReviewRequestDto;
import ua.dev.food.fast.service.domain.dto.response.ProductReviewResponseDto;
import ua.dev.food.fast.service.domain.model.product.Product;
import ua.dev.food.fast.service.domain.model.product.ProductReview;
import ua.dev.food.fast.service.exception_handler.exception.ResourceNotFoundException;
import ua.dev.food.fast.service.repository.ProductRepository;
import ua.dev.food.fast.service.repository.ProductReviewRepository;
import ua.dev.jwt.service.JwtDecodeService;

import static org.mockito.Mockito.*;

@SpringBootTest
class ProductReviewServiceTest {
    @InjectMocks
    private ProductReviewService productReviewService;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductReviewRepository productReviewRepository;
    @Mock
    private JwtDecodeService jwtDecodeService;
    @Mock
    private UserFeignClient userFeignClient;

    @Test
    void addProductReviewWhenProductExistsThenSavesReview() {
        String authHeader = "Bearer token";
        ProductReviewRequestDto requestDto = new ProductReviewRequestDto(1L, "Great product!");
        Long userId = 100L;

        when(jwtDecodeService.extractUserDataFromBearerHeader(authHeader)).thenReturn(userId);
        when(productRepository.findById(requestDto.idProduct())).thenReturn(Mono.just(mock(Product.class)));
        when(productReviewRepository.save(any(ProductReview.class))).thenReturn(Mono.just(new ProductReview()));

        Mono<Void> result = productReviewService.addProductReview(authHeader, requestDto);

        StepVerifier.create(result)
            .verifyComplete();

        verify(productRepository).findById(requestDto.idProduct());
        verify(productReviewRepository).save(any(ProductReview.class));
    }

    @Test
    void addProductReviewWhenProductNotFoundThenThrowsException() {
        String authHeader = "Bearer token";
        ProductReviewRequestDto requestDto = new ProductReviewRequestDto(1L, "Great product!");
        Long userId = 100L;

        when(jwtDecodeService.extractUserDataFromBearerHeader(authHeader)).thenReturn(userId);
        when(productRepository.findById(requestDto.idProduct())).thenReturn(Mono.empty());

        Mono<Void> result = productReviewService.addProductReview(authHeader, requestDto);

        StepVerifier.create(result)
            .expectError(ResourceNotFoundException.class)
            .verify();

        verify(productRepository).findById(requestDto.idProduct());
        verify(productReviewRepository, never()).save(any(ProductReview.class));
    }

    @Test
    void getAllProductReviewWhenProductReviewsExistThenReturnsReviews() {
        Long productId = 1L;
        ProductReview productReview = ProductReview.builder()
            .id(1L)
            .review("Great!")
            .productId(productId)
            .userId(100L)
            .build();

        when(productReviewRepository.findByProductIdOrderByProductIdAsc(productId)).thenReturn(Flux.just(productReview));
        when(userFeignClient.getUserById(100L)).thenReturn(Mono.just(UserDto.builder().lastName("Doe").firstName("John")
            .email("john@example.com")
            .build()));

        Flux<ProductReviewResponseDto> result = productReviewService.getAllProductReview(productId);

        StepVerifier.create(result)
            .expectNextMatches(response -> response.review().equals("Great!") && response.emailReviewer()
                .equals("john@example.com"))
            .verifyComplete();

        verify(productReviewRepository).findByProductIdOrderByProductIdAsc(productId);
        verify(userFeignClient).getUserById(100L);
    }

    @Test
    void deleteProductReviewWhenReviewNotFoundThenThrowsException() {
        String authHeader = "Bearer token";
        Long productId = 1L;
        Long reviewId = 2L;
        Long userId = 100L;

        when(jwtDecodeService.extractUserDataFromBearerHeader(authHeader)).thenReturn(userId);
        when(productRepository.findById(productId)).thenReturn(Mono.empty());

        Mono<Void> result = productReviewService.deleteProductReview(authHeader, productId, reviewId);

        StepVerifier.create(result)
            .expectError(ResourceNotFoundException.class)
            .verify();

        verify(productRepository).findById(productId);
    }
}
