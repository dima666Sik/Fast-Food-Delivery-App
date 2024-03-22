package dev.food.fast.server.general.repository;

import dev.food.fast.server.auth.models.User;
import dev.food.fast.server.general.models.order.BasicOrderUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BasicOrderUserRepository extends JpaRepository<BasicOrderUser, Long> {

    List<BasicOrderUser> findByUserOrderByIdDesc(User user);
}
