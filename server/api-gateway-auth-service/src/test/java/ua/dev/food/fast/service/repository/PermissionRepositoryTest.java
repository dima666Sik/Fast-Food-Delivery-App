package ua.dev.food.fast.service.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ua.dev.food.fast.service.domain.model.Permission;
import ua.dev.food.fast.service.domain.model.Role;

@DataR2dbcTest
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = {PostgreSQLContainerInitializer.class})
class PermissionRepositoryTest {
    @Autowired
    private PermissionRepository permissionRepository;

    @Test
    void testFindPermissionByRoleName() {
        Mono<Permission> permission = permissionRepository.findPermissionByRole(Role.USER.name());
        StepVerifier.create(permission).expectNextMatches(p -> Role.USER.name().equals(p.getRole().name()))
            .verifyComplete();
    }
}