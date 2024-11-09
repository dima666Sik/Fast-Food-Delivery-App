package ua.dev.food.fast.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

class TestContainerTests {
    @Test
    void mySQLContainerShouldBeRunning() {
        try (
            var postgreSQLContainer = new PostgreSQLContainer<>("postgres:12")
                .withDatabaseName("test_db")
                .withUsername("test")
                .withPassword("test");
        ) {
            postgreSQLContainer.start();
            Assertions.assertTrue(postgreSQLContainer.isRunning());
        }
    }
}
