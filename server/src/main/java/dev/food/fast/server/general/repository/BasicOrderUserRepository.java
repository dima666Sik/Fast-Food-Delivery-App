package dev.food.fast.server.general.repository;

import dev.food.fast.server.general.models.order.BasicOrderUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasicOrderUserRepository extends JpaRepository<BasicOrderUser, Long> {
}
