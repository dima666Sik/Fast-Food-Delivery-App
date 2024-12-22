package ua.dev.food.fast.service.repository;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

public class PostgreSQLContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:12")
        .withDatabaseName("test_db")
        .withUsername("test")
        .withPassword("test");

    static {
        postgreSQLContainer.start();
    }

    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        TestPropertyValues.of(
            "spring.r2dbc.url=" + "r2dbc:postgresql://" + postgreSQLContainer.getHost() + ":" + postgreSQLContainer.getFirstMappedPort() + "/" + postgreSQLContainer.getDatabaseName(),
            "spring.r2dbc.username=" + postgreSQLContainer.getUsername(),
            "spring.r2dbc.password=" + postgreSQLContainer.getPassword(),
            "spring.liquibase.url=" + postgreSQLContainer.getJdbcUrl(),
            "spring.liquibase.user=" + postgreSQLContainer.getUsername(),
            "spring.liquibase.password=" + postgreSQLContainer.getPassword()
        ).applyTo(configurableApplicationContext.getEnvironment());
    }
}
