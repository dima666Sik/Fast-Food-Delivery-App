package ua.dev.food.fast.service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.dev.feign.clients.ProductResponseDto;
import ua.dev.food.fast.service.domain.dto.response.ProductReviewResponseDto;
import ua.dev.food.fast.service.domain.model.product.Product;
import ua.dev.food.fast.service.service.ProductReviewService;
import ua.dev.food.fast.service.service.ProductSortedService;
import ua.dev.food.fast.service.service.ProductService;
import ua.dev.food.fast.service.util.DefaultProductsData;
import ua.dev.food.fast.service.util.MessageConstants;

import java.util.List;

@RestController
@RequestMapping("/api/v2/product/foods")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final ProductSortedService productSortedService;
    private final ProductReviewService productReviewService;

    @PostMapping("/add-all-default-products")
    public Mono<ResponseEntity<String>> addAllDefaultProducts() {
        // Get all default products
        return Mono.just(DefaultProductsData.getAllDefaultProducts())
            .flatMapMany(Flux::fromIterable) // Convert the List<Product> to Flux<Product>
            .flatMap(productService::addDefaultProduct) // Process each product
            .then(Mono.just(ResponseEntity.ok(MessageConstants.PRODUCTS_ADDED_SUCCESSFULLY))); // Return success response
    }


    @GetMapping("/product/{id}")
    public Mono<ResponseEntity<ProductResponseDto>> getProductById(@PathVariable("id") Long id) {
        return productService.getProductById(id).map(ResponseEntity::ok);
    }

    @GetMapping("/get-all-products")
    public Mono<ResponseEntity<Flux<ProductResponseDto>>> getAllProducts(
    ) {
        return Mono.just(ResponseEntity.ok().body(productSortedService.getAllProductsDefaultOrder()));
    }

    @GetMapping("/get-all-reviews-to-product")
    public Mono<ResponseEntity<Flux<ProductReviewResponseDto>>> getAllProductReview(
        @RequestParam("product_id") Long productId
    ) {
        return Mono.just(ResponseEntity.status(HttpStatus.OK)
            .body(productReviewService.getAllProductReview(productId)));
    }
}
