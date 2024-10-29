package ua.dev.food.fast.service.repository;


import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import ua.dev.food.fast.service.domain.model.User;

public interface UserRepository extends ReactiveCrudRepository<User, Long> {

  Mono<User> findByEmail(@Param("email") String email);

}
