package dev.food.fast.server.general.repository;

import dev.food.fast.server.general.models.order.BasicOrderUser;
import dev.food.fast.server.general.models.order.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    List<Purchase> findByBasicOrderUser(BasicOrderUser orderUser);
}
