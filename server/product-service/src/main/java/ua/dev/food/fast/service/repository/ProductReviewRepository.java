package ua.dev.food.fast.service.repository;

import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.dev.food.fast.service.domain.model.product.ProductReview;

public interface ProductReviewRepository extends ReactiveCrudRepository<ProductReview, Long> {
    Flux<ProductReview> findByProductIdOrderByProductIdAsc(@Param("product_id") Long productId);

    Mono<ProductReview> findByIdAndProductIdAndUserId(Long id, Long productId, Long userId);
}