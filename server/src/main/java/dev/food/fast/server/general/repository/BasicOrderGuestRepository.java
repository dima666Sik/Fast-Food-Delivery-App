package dev.food.fast.server.general.repository;

import dev.food.fast.server.general.models.order.BasicOrderGuest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasicOrderGuestRepository extends JpaRepository<BasicOrderGuest, Long> {
}
