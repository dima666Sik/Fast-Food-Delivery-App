package ua.dev.food.fast.service.service;


import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import ua.dev.food.fast.service.domain.model.product.Product;
import ua.dev.food.fast.service.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
class ProductSortedServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductSortedService productSortedService;

    private final List<Product> products = List.of(
        Product.builder()
            .title("Pizza")
            .price(new BigDecimal("50.0"))
            .likes(10L)
            .image01("image1.jpg")
            .image02("image2.jpg")
            .image03("image3.jpg")
            .category("Category")
            .description("Description")
            .build(),
        Product.builder()
            .title("Burger")
            .price(new BigDecimal("30.0"))
            .likes(20L)
            .image01("image1.jpg")
            .image02("image2.jpg")
            .image03("image3.jpg")
            .category("Category")
            .description("Description")
            .build(),
        Product.builder()
            .title("Sushi")
            .price(new BigDecimal("70.0"))
            .likes(5L)
            .image01("image1.jpg")
            .image02("image2.jpg")
            .image03("image3.jpg")
            .category("Category")
            .description("Description")
            .build()
    );

    @Test
    void testGetAllProductsDefaultOrder() {
        when(productRepository.findAll()).thenReturn(Flux.fromIterable(products));

        StepVerifier.create(productSortedService.getAllProductsDefaultOrder())
            .expectNextMatches(productDto -> productDto.title().equals("Pizza"))
            .expectNextMatches(productDto -> productDto.title().equals("Burger"))
            .expectNextMatches(productDto -> productDto.title().equals("Sushi"))
            .verifyComplete();
    }

    @Test
    void testGetAllSortedProductsByTitleAscendingOrder() {
        when(productRepository.findAllByOrderByTitleAsc()).thenReturn(Flux.fromIterable(
            products.stream().sorted((p1, p2) -> p1.getTitle().compareToIgnoreCase(p2.getTitle())).toList()
        ));

        StepVerifier.create(productSortedService.getAllSortedProductsByTitleAscendingOrder())
            .expectNextMatches(productDto -> productDto.title().equals("Burger"))
            .expectNextMatches(productDto -> productDto.title().equals("Pizza"))
            .expectNextMatches(productDto -> productDto.title().equals("Sushi"))
            .verifyComplete();
    }

    @Test
    void testGetAllSortedProductsByTitleDescendingOrder() {
        when(productRepository.findAllByOrderByTitleDesc()).thenReturn(Flux.fromIterable(
            products.stream().sorted((p1, p2) -> p2.getTitle().compareToIgnoreCase(p1.getTitle())).toList()
        ));

        StepVerifier.create(productSortedService.getAllSortedProductsByTitleDescendingOrder())
            .expectNextMatches(productDto -> productDto.title().equals("Sushi"))
            .expectNextMatches(productDto -> productDto.title().equals("Pizza"))
            .expectNextMatches(productDto -> productDto.title().equals("Burger"))
            .verifyComplete();
    }

    @Test
    void testGetAllSortedProductsByPriceDescendingOrder() {
        when(productRepository.findAllByOrderByPriceDesc()).thenReturn(Flux.fromIterable(
            products.stream().sorted((p1, p2) -> p2.getPrice().compareTo(p1.getPrice())).toList()
        ));

        StepVerifier.create(productSortedService.getAllSortedProductsByPriceDescendingOrder())
            .expectNextMatches(productDto -> productDto.price().equals(new BigDecimal("70.0")))
            .expectNextMatches(productDto -> productDto.price().equals(new BigDecimal("50.0")))
            .expectNextMatches(productDto -> productDto.price().equals(new BigDecimal("30.0")))
            .verifyComplete();
    }

    @Test
    void testGetAllSortedProductsByPriceAscendingOrder() {
        when(productRepository.findAllByOrderByPriceAsc()).thenReturn(Flux.fromIterable(
            products.stream().sorted(Comparator.comparing(Product::getPrice)).toList()
        ));

        StepVerifier.create(productSortedService.getAllSortedProductsByPriceAscendingOrder())
            .expectNextMatches(productDto -> productDto.price().equals(new BigDecimal("30.0")))
            .expectNextMatches(productDto -> productDto.price().equals(new BigDecimal("50.0")))
            .expectNextMatches(productDto -> productDto.price().equals(new BigDecimal("70.0")))
            .verifyComplete();
    }

    @Test
    void testGetAllSortedProductsByLikesAscendingOrder() {
        when(productRepository.findAllByOrderByLikesAsc()).thenReturn(Flux.fromIterable(
            products.stream().sorted(Comparator.comparing(Product::getLikes)).toList()
        ));

        StepVerifier.create(productSortedService.getAllSortedProductsByLikesAscendingOrder())
            .expectNextMatches(productDto -> productDto.likes() == 5L)
            .expectNextMatches(productDto -> productDto.likes() == 10L)
            .expectNextMatches(productDto -> productDto.likes() == 20L)
            .verifyComplete();
    }

    @Test
    void testGetAllSortedProductsByLikesDescendingOrder() {
        when(productRepository.findAllByOrderByLikesDesc()).thenReturn(Flux.fromIterable(
            products.stream().sorted((p1, p2) -> p2.getLikes().compareTo(p1.getLikes())).toList()
        ));

        StepVerifier.create(productSortedService.getAllSortedProductsByLikesDescendingOrder())
            .expectNextMatches(productDto -> productDto.likes() == 20L)
            .expectNextMatches(productDto -> productDto.likes() == 10L)
            .expectNextMatches(productDto -> productDto.likes() == 5L)
            .verifyComplete();
    }
}
