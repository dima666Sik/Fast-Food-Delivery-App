package ua.dev.food.fast.service.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ua.dev.food.fast.service.domain.dto.request.ProductStatusLikedRequestDto;
import ua.dev.food.fast.service.domain.dto.request.ProductStatusRequestDto;
import ua.dev.food.fast.service.domain.dto.response.ProductStatusLikesResponseDto;
import ua.dev.food.fast.service.domain.model.product.Product;
import ua.dev.food.fast.service.domain.model.product.ProductStatusLikes;
import ua.dev.food.fast.service.repository.ProductRepository;
import ua.dev.food.fast.service.repository.ProductStatusLikesRepository;
import ua.dev.jwt.service.JwtDecodeService;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class ProductStatusLikesServiceTest {
    @Mock
    private JwtDecodeService jwtDecodeService;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductStatusLikesRepository productLikesRepository;
    @InjectMocks
    private ProductStatusLikesService productStatusLikesService;

    @Test
    void testSetStatusOnProductNewStatus() {
        String authHeader = "Bearer token";
        ProductStatusRequestDto requestDto = new ProductStatusRequestDto(1L, true);

        Long userId = 10L;
        Product product = Product.builder().id(1L).build();
        ProductStatusLikes newStatus = ProductStatusLikes.builder().userId(userId).productId(1L).status(true).build();

        // Mock behavior
        when(jwtDecodeService.extractUserDataFromBearerHeader(authHeader)).thenReturn(userId);
        when(productRepository.findById(1L)).thenReturn(Mono.just(product));
        when(productLikesRepository.findByProductIdAndUserId(1L, userId)).thenReturn(Mono.empty());
        when(productLikesRepository.save(Mockito.any())).thenReturn(Mono.just(newStatus));

        // Execute the method
        StepVerifier.create(productStatusLikesService.setStatusOnProduct(authHeader, requestDto))
            .verifyComplete();

        // Verify interactions
        verify(productRepository).findById(1L);
        verify(productLikesRepository).findByProductIdAndUserId(1L, userId);
    }

    @Test
    void testSetStatusOnProductUpdateExistingStatus() {
        String authHeader = "Bearer token";
        ProductStatusRequestDto requestDto = new ProductStatusRequestDto(1L, false);

        Long userId = 10L;
        Product product = Product.builder().id(1L).build();
        ProductStatusLikes existingStatus = ProductStatusLikes.builder().userId(userId).productId(1L).status(true)
            .build();

        // Mock behavior
        when(jwtDecodeService.extractUserDataFromBearerHeader(authHeader)).thenReturn(userId);
        when(productRepository.findById(1L)).thenReturn(Mono.just(product));
        when(productLikesRepository.findByProductIdAndUserId(1L, userId)).thenReturn(Mono.just(existingStatus));
        when(productLikesRepository.save(existingStatus)).thenReturn(Mono.just(existingStatus));

        // Execute the method
        StepVerifier.create(productStatusLikesService.setStatusOnProduct(authHeader, requestDto))
            .verifyComplete();

        // Verify status was updated
        Assertions.assertEquals(false, existingStatus.getStatus());

        // Verify interactions
        verify(productRepository).findById(1L);
        verify(productLikesRepository).findByProductIdAndUserId(1L, userId);
        verify(productLikesRepository).save(existingStatus);
    }

    @Test
    void testUpdateLikeOnProduct() {
        ProductStatusLikedRequestDto likedRequest = new ProductStatusLikedRequestDto(1L, 50L);
        Product product = Product.builder().id(1L).likes(10L).build();

        // Mock behavior
        when(productRepository.findById(1L)).thenReturn(Mono.just(product));
        when(productRepository.save(product)).thenReturn(Mono.just(product));

        // Execute the method
        StepVerifier.create(productStatusLikesService.updateLikeOnProduct(likedRequest))
            .verifyComplete();

        // Verify likes were updated
        Assertions.assertEquals(50L, product.getLikes());

        // Verify interactions
        verify(productRepository).findById(1L);
        verify(productRepository).save(product);
    }

    @Test
    void testGetListStatusLikesProducts() {
        String authHeader = "Bearer token";
        Long userId = 10L;
        Product product = Product.builder().id(1L).likes(100L).build();
        ProductStatusLikes productStatusLikes = ProductStatusLikes.builder().id(1L).productId(1L).status(true).build();
        ProductStatusLikesResponseDto responseDto = ProductStatusLikesResponseDto.builder().idProduct(1L).likes(100L)
            .status(true).build();

        // Mock behavior
        when(jwtDecodeService.extractUserDataFromBearerHeader(authHeader)).thenReturn(userId);
        when(productLikesRepository.findByUserId(userId)).thenReturn(Flux.just(productStatusLikes));
        when(productRepository.findById(1L)).thenReturn(Mono.just(product));

        // Execute the method
        StepVerifier.create(productStatusLikesService.getListStatusLikesProducts(authHeader))
            .expectNext(responseDto)
            .verifyComplete();

        // Verify interactions
        verify(productLikesRepository).findByUserId(userId);
        verify(productRepository).findById(1L);
    }
}
