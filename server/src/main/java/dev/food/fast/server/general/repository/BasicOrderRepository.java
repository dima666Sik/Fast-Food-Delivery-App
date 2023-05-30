package dev.food.fast.server.general.repository;

import dev.food.fast.server.general.models.order.BasicOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasicOrderRepository extends JpaRepository<BasicOrder, Integer> {
}
