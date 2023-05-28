package dev.food.fast.server.auth.repository;

import java.util.List;
import java.util.Optional;

import dev.food.fast.server.auth.dto.UserDTO;
import dev.food.fast.server.auth.models.User;
import dev.food.fast.server.general.dto.ProductReviewDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Integer> {

  Optional<User> findByEmail(String email);

  @Query("SELECT new dev.food.fast.server.auth.dto.UserDTO(u.firstname, u.lastname, u.email) FROM User u WHERE u.id = :user_id")
  Optional<UserDTO> findDTOByUser_Id(@Param("user_id") Integer userId);

}
