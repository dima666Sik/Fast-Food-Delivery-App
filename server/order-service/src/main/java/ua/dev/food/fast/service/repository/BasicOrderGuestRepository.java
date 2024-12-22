package ua.dev.food.fast.service.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import ua.dev.food.fast.service.domain.model.BasicOrderGuest;
import ua.dev.food.fast.service.domain.model.BasicOrderUser;

public interface BasicOrderGuestRepository extends ReactiveCrudRepository<BasicOrderGuest, Long> {
    Mono<BasicOrderGuest> findByBasicOrderId(Long basicOrderId);
}
