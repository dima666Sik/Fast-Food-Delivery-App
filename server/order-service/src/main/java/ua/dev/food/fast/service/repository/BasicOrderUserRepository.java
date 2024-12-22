package ua.dev.food.fast.service.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.dev.food.fast.service.domain.model.BasicOrderUser;

public interface BasicOrderUserRepository extends ReactiveCrudRepository<BasicOrderUser, Long> {
    Flux<BasicOrderUser> findBasicOrderUserByUserId(Long userId);

    Mono<BasicOrderUser> findByBasicOrderId(Long basicOrderId);
}
