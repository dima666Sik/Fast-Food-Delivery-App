package ua.dev.food.fast.service.repository;

import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import ua.dev.food.fast.service.domain.model.Status;

public interface StatusRepository extends ReactiveCrudRepository<Status, Long> {
    Mono<Status> findStatusByState(@Param("state") String state);
}
