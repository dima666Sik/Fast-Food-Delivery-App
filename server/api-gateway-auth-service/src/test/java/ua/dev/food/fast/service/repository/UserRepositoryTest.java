package ua.dev.food.fast.service.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
import ua.dev.food.fast.service.domain.model.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataR2dbcTest
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = {PostgreSQLContainerInitializer.class})
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    private User user;

    @BeforeEach
    public void setUp() {
        // Initialize sample data
        user = User.builder().email("lg@gmail.com").firstName("Dima").lastName("M")
            .permissions(List.of(Permission.builder().role(Role.USER).build())).password("****").build();
        user = userRepository.save(user).block();

    }

    @AfterEach
    void tearDown() {
        userRepository.deleteById(user.getId()).block();
    }

    @Test
    void testFindByEmail() {
        Mono<User> result = userRepository.findByEmail(user.getEmail());

        StepVerifier.create(result).expectNextMatches(receivedUser -> user.getEmail().equals(receivedUser.getEmail()))
            .verifyComplete();
    }
}