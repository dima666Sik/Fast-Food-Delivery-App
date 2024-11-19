package ua.dev.food.fast.service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import ua.dev.feign.clients.ProductResponseDto;
import ua.dev.food.fast.service.domain.model.mapper.Mapper;
import ua.dev.food.fast.service.repository.ProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductSortedService {
    private final ProductRepository productRepository;

    public Flux<ProductResponseDto> getAllProductsDefaultOrder() {
        return productRepository.findAll()
            .map(Mapper::productToProductResponseDto);
    }

    public Flux<ProductResponseDto> getAllSortedProductsByTitleAscendingOrder() {
        return productRepository.findAllByOrderByTitleAsc()
            .map(Mapper::productToProductResponseDto);
    }

    public Flux<ProductResponseDto> getAllSortedProductsByTitleDescendingOrder() {
        return productRepository.findAllByOrderByTitleDesc()
            .map(Mapper::productToProductResponseDto);
    }

    public Flux<ProductResponseDto> getAllSortedProductsByPriceDescendingOrder() {
        return productRepository.findAllByOrderByPriceDesc()
            .map(Mapper::productToProductResponseDto);
    }

    public Flux<ProductResponseDto> getAllSortedProductsByPriceAscendingOrder() {
        return productRepository.findAllByOrderByPriceAsc()
            .map(Mapper::productToProductResponseDto);
    }

    public Flux<ProductResponseDto> getAllSortedProductsByLikesDescendingOrder() {
        return productRepository.findAllByOrderByLikesDesc()
            .map(Mapper::productToProductResponseDto);
    }

    public Flux<ProductResponseDto> getAllSortedProductsByLikesAscendingOrder() {
        return productRepository.findAllByOrderByLikesAsc()
            .map(Mapper::productToProductResponseDto);
    }

}
