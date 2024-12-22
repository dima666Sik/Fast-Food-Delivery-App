package ua.dev.food.fast.service.repository;

import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import ua.dev.food.fast.service.domain.model.StatusOrder;

public interface StatusOrderRepository extends ReactiveCrudRepository<StatusOrder, Long> {
    Mono<StatusOrder> findByOrderId(@Param("order_id") Long orderId);
}
