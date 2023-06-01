package dev.food.fast.server.general.repository;

import dev.food.fast.server.general.models.order.AddressOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressOrderRepository extends JpaRepository<AddressOrder, Long> {
}
