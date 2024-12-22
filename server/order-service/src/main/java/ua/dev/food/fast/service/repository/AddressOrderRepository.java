package ua.dev.food.fast.service.repository;


import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import ua.dev.food.fast.service.domain.model.AddressOrder;

public interface AddressOrderRepository extends ReactiveCrudRepository<AddressOrder, Long> {
}
