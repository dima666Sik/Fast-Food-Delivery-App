package ua.dev.food.fast.service.repository;

import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.dev.food.fast.service.domain.model.product.ProductStatusLikes;


public interface ProductStatusLikesRepository extends ReactiveCrudRepository<ProductStatusLikes, Long> {
    Mono<ProductStatusLikes> findByProductIdAndUserId(Long productId, Long userId);

    Flux<ProductStatusLikes> findByUserId(@Param("userId") Long userId);
}

