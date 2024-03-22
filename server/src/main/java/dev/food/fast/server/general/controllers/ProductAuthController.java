package dev.food.fast.server.general.controllers;

import dev.food.fast.server.general.dto.request.ProductRequest;
import dev.food.fast.server.general.service.ProductsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin-foods")
@RequiredArgsConstructor
public class ProductAuthController {
    private final ProductsService service;

    @PostMapping(value = "/add-product", produces = {MediaType.IMAGE_PNG_VALUE, "application/json"})
    public ResponseEntity<?> addProduct(@ModelAttribute ProductRequest request) {
        return service.addProduct(request);

    }


}
