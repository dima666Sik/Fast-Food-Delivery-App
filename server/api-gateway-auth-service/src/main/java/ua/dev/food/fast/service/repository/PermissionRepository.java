package ua.dev.food.fast.service.repository;

import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import ua.dev.food.fast.service.domain.model.Permission;

public interface PermissionRepository extends ReactiveCrudRepository<Permission, Long> {
    Mono<Permission> findPermissionByRole(@Param("role") String role);
}
