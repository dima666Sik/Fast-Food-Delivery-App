package ua.dev.food.fast.service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.dev.food.fast.service.domain.dto.request.ProductRequestDto;
import ua.dev.food.fast.service.service.ProductService;
import ua.dev.food.fast.service.util.MessageConstants;

@RestController
@RequestMapping("/api/v2/product/admin-foods")
@RequiredArgsConstructor
public class ProductAuthController {
    private final ProductService service;

    //    @PostMapping(value = "/add-product", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PostMapping(value = "/add-product")
    public Mono<ResponseEntity<String>> addProduct(
        @RequestPart("files") Flux<FilePart> files,
        @RequestPart("data") ProductRequestDto request
    ) {
        return service.addProduct(files, request)
            .then(Mono.fromCallable(() -> ResponseEntity.ok(MessageConstants.PRODUCT_ADDED_SUCCESSFULLY)));
    }
}
