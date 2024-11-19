package ua.dev.food.fast.service.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.dev.food.fast.service.domain.model.UserPermission;

public interface UserPermissionRepository extends ReactiveCrudRepository<UserPermission, Long> {
    Flux<UserPermission> findAllByUserId(@Param("user_id") Long userId);

    Mono<Object> deleteAllByUserId(@Param("user_id") Long id);
}
