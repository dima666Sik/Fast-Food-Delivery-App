package ua.dev.food.fast.service.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import ua.dev.food.fast.service.domain.model.Purchase;

public interface PurchaseRepository extends ReactiveCrudRepository<Purchase, Long> {
    Flux<Purchase> findByBasicOrderUserId(Long orderUserId);

    Flux<Purchase> findByBasicOrderGuestId(Long orderUserId);
}
