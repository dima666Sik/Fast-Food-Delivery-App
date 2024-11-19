package ua.dev.food.fast.service.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import ua.dev.food.fast.service.domain.model.product.Product;

public interface ProductRepository extends ReactiveCrudRepository<Product, Long> {

    // Sort products by title in ascending order
    Flux<Product> findAllByOrderByTitleAsc();

    // Sort products by title in descending order
    Flux<Product> findAllByOrderByTitleDesc();

    // Sort products by price in ascending order
    Flux<Product> findAllByOrderByPriceAsc();

    // Sort products by price in descending order
    Flux<Product> findAllByOrderByPriceDesc();

    // Sort products by likes in ascending order
    Flux<Product> findAllByOrderByLikesAsc();

    // Sort products by likes in descending order
    Flux<Product> findAllByOrderByLikesDesc();
}
