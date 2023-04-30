package dev.food.fast.server.general.controllers;

import dev.food.fast.server.general.service.ProductsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/foods/sorted/")
@RequiredArgsConstructor
public class ProductsSortedController {
    private final ProductsService service;

    @GetMapping("/ascending")
    public ResponseEntity<?> getAllSortedProductsAZ(
    ) {
        return service.getAllSortedProductsAZ();
    }

    @GetMapping("/descending")
    public ResponseEntity<?> getAllSortedProductsZA(
    ) {
        return service.getAllSortedProductsZA();
    }

    @GetMapping("/high-price")
    public ResponseEntity<?> getAllSortedProductsHighPrice(
    ) {
        return service.getAllSortedProductsHighPrice();
    }

    @GetMapping("/low-price")
    public ResponseEntity<?> getAllSortedProductsLowPrice(
    ) {
        return service.getAllSortedProductsLowPrice();
    }
    @GetMapping("/high-likes")
    public ResponseEntity<?> getAllSortedProductsHighLikes(
    ) {
        return service.getAllSortedProductsHighLikes();
    }

    @GetMapping("/low-likes")
    public ResponseEntity<?> getAllSortedProductsLowLikes(
    ) {
        return service.getAllSortedProductsLowLikes();
    }

}
