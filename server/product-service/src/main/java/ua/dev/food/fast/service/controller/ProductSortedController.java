package ua.dev.food.fast.service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.dev.feign.clients.ProductResponseDto;
import ua.dev.food.fast.service.service.ProductSortedService;

import java.util.List;

@RestController
@RequestMapping("/api/v2/product/foods/sort")
@RequiredArgsConstructor
public class ProductSortedController {

    private final ProductSortedService productSortedService;

    @GetMapping("/ascending")
    public Mono<ResponseEntity<Flux<ProductResponseDto>>> getAllSortedProductsByTitleAscendingOrder() {
        return Mono.just(ResponseEntity.ok(productSortedService.getAllSortedProductsByTitleAscendingOrder()));
    }

    @GetMapping("/descending")
    public Mono<ResponseEntity<Flux<ProductResponseDto>>> getAllSortedProductsByTitleDescendingOrder() {
        return Mono.just(ResponseEntity.ok(productSortedService.getAllSortedProductsByTitleDescendingOrder()));
    }

    @GetMapping("/high-price")
    public Mono<ResponseEntity<Flux<ProductResponseDto>>> getAllSortedProductsByPriceDescendingOrder() {
        return Mono.just(ResponseEntity.ok(productSortedService.getAllSortedProductsByPriceDescendingOrder()));
    }

    @GetMapping("/low-price")
    public Mono<ResponseEntity<Flux<ProductResponseDto>>> getAllSortedProductsByPriceAscendingOrder() {
        return Mono.just(ResponseEntity.ok(productSortedService.getAllSortedProductsByPriceAscendingOrder()));
    }

    @GetMapping("/high-likes")
    public Mono<ResponseEntity<Flux<ProductResponseDto>>> getAllSortedProductsByLikesDescendingOrder() {
        return Mono.just(ResponseEntity.ok(productSortedService.getAllSortedProductsByLikesDescendingOrder()));
    }

    @GetMapping("/low-likes")
    public Mono<ResponseEntity<Flux<ProductResponseDto>>> getAllSortedProductsByLikesAscendingOrder() {
        return Mono.just(ResponseEntity.ok(productSortedService.getAllSortedProductsByLikesAscendingOrder()));
    }
}

