package dev.food.fast.server.general.controllers;

import dev.food.fast.server.general.models.product.Product;
import dev.food.fast.server.general.dto.request.ProductRequest;
import dev.food.fast.server.general.service.ProductReviewService;
import dev.food.fast.server.general.service.ProductsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/foods")
@RequiredArgsConstructor
public class ProductsController {
    private final ProductsService service;
    private final ProductReviewService productReviewService;

    @PostMapping("/add-product")
    public ResponseEntity<?> addProduct(@RequestBody ProductRequest request) {
        return service.addProduct(request);
    }


    @PostMapping("/add-all-default-products")
    public ResponseEntity<?> addAllDefaultProducts() {
        List<Product> products = service.getAllDefaultProducts();

        for (Product product : products) {
            ResponseEntity<?> response = service.addProduct(product);
        }

        return ResponseEntity.ok("Products added successfully");

    }

    @GetMapping("/get-all-products")
    public ResponseEntity<?> getAllProducts(
    ) {
        return service.getAllProducts();
    }

    @GetMapping("/get-all-reviews-to-product")
    public ResponseEntity<?> getAllProductReview(@RequestParam("product_id") Long productId
    ) {
        return productReviewService.getAllProductReview(productId);
    }
}
