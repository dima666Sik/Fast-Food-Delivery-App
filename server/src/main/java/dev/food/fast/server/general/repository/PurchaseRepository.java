package dev.food.fast.server.general.repository;

import dev.food.fast.server.general.models.order.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {
}
