package dev.food.fast.server.auth.repository;

import java.util.Optional;

import dev.food.fast.server.auth.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

  Optional<User> findByEmail(String email);

}
