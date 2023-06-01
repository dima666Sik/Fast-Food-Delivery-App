package dev.food.fast.server.general.service;

import dev.food.fast.server.general.models.product.Product;
import dev.food.fast.server.general.dto.request.ProductRequest;
import dev.food.fast.server.general.repository.ProductsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductsService {
    private final ProductsRepository productsRepository;

    public ResponseEntity<?>  addProduct(ProductRequest request) {
        var product = Product.builder()
                .title(request.getTitle())
                .price(request.getPrice())
                .likes(request.getLikes())
                .image01(request.getImage01())
                .image02(request.getImage02())
                .image03(request.getImage03())
                .category(request.getCategory())
                .description(request.getDescription())
                .build();
        productsRepository.save(product);
        return ResponseEntity.ok("Image added successfully");
    }

    public ResponseEntity<?>  addProduct(Product request) {
        var product = Product.builder()
                .title(request.getTitle())
                .price(request.getPrice())
                .likes(request.getLikes())
                .image01(request.getImage01())
                .image02(request.getImage02())
                .image03(request.getImage03())
                .category(request.getCategory())
                .description(request.getDescription())
                .build();
        productsRepository.save(product);
        return ResponseEntity.ok("Image added successfully");
    }

    public ResponseEntity<?> getAllProducts() {
        List<Product> products = productsRepository.findAll();
        return ResponseEntity.ok(products);
    }

    public ResponseEntity<?> getAllSortedProductsAZ() {
        List<Product> products = productsRepository.findAll(Sort.by("title").ascending());
        return ResponseEntity.ok(products);
    }

    public ResponseEntity<?> getAllSortedProductsZA() {
        List<Product> products = productsRepository.findAll(Sort.by("title").descending());
        return ResponseEntity.ok(products);
    }

    public ResponseEntity<?> getAllSortedProductsHighPrice() {
        List<Product> products = productsRepository.findAll(Sort.by("price").descending());
        return ResponseEntity.ok(products);
    }

    public ResponseEntity<?> getAllSortedProductsLowPrice() {
        List<Product> products = productsRepository.findAll(Sort.by("price").ascending());
        return ResponseEntity.ok(products);
    }

    public ResponseEntity<?> getAllSortedProductsHighLikes() {
        List<Product> products = productsRepository.findAll(Sort.by("likes").descending());
        return ResponseEntity.ok(products);
    }

    public ResponseEntity<?> getAllSortedProductsLowLikes() {
        List<Product> products = productsRepository.findAll(Sort.by("likes").ascending());
        return ResponseEntity.ok(products);
    }
}
