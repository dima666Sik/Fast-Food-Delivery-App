package ua.dev.food.fast.service.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ua.dev.food.fast.service.domain.model.Permission;
import ua.dev.food.fast.service.domain.model.Role;
import ua.dev.food.fast.service.domain.model.User;
import ua.dev.food.fast.service.domain.model.UserPermission;

import java.util.List;

@DataR2dbcTest
@Testcontainers
@ActiveProfiles("test")
class UserPermissionRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:12").withDatabaseName("test_db")
        .withUsername("test").withPassword("test");

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        postgreSQLContainer.start();
        registry.add("spring.r2dbc.url", () -> "r2dbc:postgresql://" + postgreSQLContainer.getHost() + ":" + postgreSQLContainer.getFirstMappedPort() + "/" + postgreSQLContainer.getDatabaseName());
        registry.add("spring.r2dbc.username", postgreSQLContainer::getUsername);
        registry.add("spring.r2dbc.password", postgreSQLContainer::getPassword);
        registry.add("spring.liquibase.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.liquibase.user", postgreSQLContainer::getUsername);
        registry.add("spring.liquibase.password", postgreSQLContainer::getPassword);
    }

    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserPermissionRepository userPermissionRepository;
    private User user;

    @BeforeEach
    public void setUp() {
        // Initialize sample data
        user = User.builder().email("lg@gmail.com").firstname("Dima").lastname("M")
            .permissions(List.of(Permission.builder().role(Role.USER).build())).password("****").build();
        user = userRepository.save(user).block();

        Permission permission = permissionRepository.findPermissionByRole(Role.USER.name()).block();

        userPermissionRepository.save(UserPermission.builder().permissionId(permission.getId())
            .userId(user.getId()).build()).block();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteById(user.getId()).block();
        userPermissionRepository.deleteAllByUserId(user.getId()).block();
    }

    @Test
    void testFindAllByUserId() {
        StepVerifier.create(userPermissionRepository.findAllByUserId(user.getId())).expectNextCount(1).verifyComplete();
    }
}