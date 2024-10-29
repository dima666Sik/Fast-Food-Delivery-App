package ua.dev.food.fast.service.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import ua.dev.food.fast.service.domain.model.UserPermission;

public interface UserPermissionRepository extends ReactiveCrudRepository<UserPermission, Long> {
    @Query(value = """
        select user_id, permission_id from user_permissions where user_id = :user_id
        """)
    Flux<UserPermission> findAllByUserId(@Param("user_id") Long userId);
}
