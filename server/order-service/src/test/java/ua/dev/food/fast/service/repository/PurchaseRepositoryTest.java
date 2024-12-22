package ua.dev.food.fast.service.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

@DataR2dbcTest
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = {PostgreSQLContainerInitializer.class})
class PurchaseRepositoryTest {
    @Autowired
    private PurchaseRepository repository;

    @Autowired
    private DatabaseClient databaseClient;

    @BeforeEach
    void setupDatabase() {
        databaseClient.sql("INSERT INTO basic_order_user (id, user_id, basic_order_id) VALUES (1, 101, 101), (2, 102, 102)")
            .then().block();
        databaseClient.sql("INSERT INTO purchase (id, product_id, basic_order_user_id, basic_order_guest_id, total_price, quantity) " +
                "VALUES (1, 1, 1, null, 10, 2), (2, 1, 1, null, 10, 2)")
            .then().block();
    }

    @AfterEach
    void cleanUpDatabase() {
        databaseClient.sql("DELETE FROM purchase").then().block();
        databaseClient.sql("DELETE FROM basic_order_user").then().block();
    }

    @Test
    void testFindByBasicOrderUserId() {
        StepVerifier.create(repository.findByBasicOrderUserId(1L))
            .expectNextMatches(purchase -> purchase.getId() == 1)
            .expectNextMatches(purchase -> purchase.getId() == 2)
            .verifyComplete();

        StepVerifier.create(repository.findByBasicOrderUserId(2L))
            .verifyComplete();
    }
}