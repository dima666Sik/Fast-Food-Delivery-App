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
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ua.dev.food.fast.service.domain.model.BasicOrder;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DataR2dbcTest
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = {PostgreSQLContainerInitializer.class})
class BasicOrderUserRepositoryTest {
    @Autowired
    private BasicOrderUserRepository repository;

    @Autowired
    private DatabaseClient databaseClient;

    @BeforeEach
    void setupDatabase() {
        BasicOrder order = new BasicOrder(null, "1234567890", "2024-11-20", "10:30:00",
            BigDecimal.valueOf(150.75), 1L, true, null);
        insertBasicOrder(order).block();
        databaseClient.sql("INSERT INTO basic_order_user (id, user_id, basic_order_id) VALUES (1, 101, 101), (2, 102, 102)")
            .then().block();
    }

    public Mono<Void> insertBasicOrder(BasicOrder order) {
        return databaseClient.sql("""
                INSERT INTO basic_order (phone, order_date_arrived, order_time_arrived, total_amount, address_order_id, cash_payment)
                VALUES (:phone, :orderDateArrived, :orderTimeArrived, :totalAmount, :addressOrderId, :cashPayment)
                """)
            .bind("phone", order.getPhone())
            .bind("orderDateArrived", order.getOrderDateArrived())
            .bind("orderTimeArrived", order.getOrderTimeArrived())
            .bind("totalAmount", order.getTotalAmount())
            .bind("addressOrderId", order.getAddressOrderId())
            .bind("cashPayment", order.getCashPayment())
            .then();
    }

    @AfterEach
    void cleanUpDatabase() {
        databaseClient.sql("DELETE FROM basic_order_user").then().block();
        databaseClient.sql("DELETE FROM basic_order").then().block();
    }

    @Test
    void testFindBasicOrderUserByUserId() {
        StepVerifier.create(repository.findBasicOrderUserByUserId(101L))
            .expectNextMatches(user -> user.getUserId().equals(101L))
            .verifyComplete();

        StepVerifier.create(repository.findBasicOrderUserByUserId(103L))
            .verifyComplete(); // Should return empty
    }
}