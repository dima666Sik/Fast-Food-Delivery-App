package ua.dev.food.fast.service.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import ua.dev.food.fast.service.domain.model.ProcessingOrder;
import ua.dev.food.fast.service.domain.model.StatusOrder;

public interface ProcessingOrderRepository extends ReactiveCrudRepository<ProcessingOrder, Long> {
    Flux<ProcessingOrder> findAllByCourierId(Long courierId);
}
