package ua.dev.food.fast.service.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ua.dev.feign.clients.UserDto;
import ua.dev.feign.clients.UserFeignClient;
import ua.dev.food.fast.service.domain.model.product.Product;
import ua.dev.food.fast.service.domain.model.product.ProductStatusLikes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DataR2dbcTest
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = {PostgreSQLContainerInitializer.class})
class ProductStatusLikesRepositoryTest {
    @Autowired
    private ProductStatusLikesRepository productStatusLikesRepository;

    @Autowired
    private DatabaseClient databaseClient; // For seeding test data

    @BeforeEach
    void setUp() {
        databaseClient.sql("""
                INSERT INTO products (id, title, price, likes, image01, image02, image03, category, description)
                VALUES
                  (1, 'Pizza Margherita', 9.99, 100, 'image1.png', 'image2.png', 'image3.png', 'Food', 'A delicious pizza with fresh ingredients'),
                  (2, 'Cheeseburger', 5.49, 50, 'burger1.png', 'burger2.png', 'burger3.png', 'Fast Food', 'A tasty cheeseburger with cheese and lettuce')
                """)
            .fetch().rowsUpdated().block();
        databaseClient.sql("""
                INSERT INTO product_status_likes (id, product_id, user_id) VALUES
                (1, 1, 1),
                (2, 2, 2)
                """)
            .fetch().rowsUpdated().block();
    }

    @AfterEach
    void tearDown() {
        databaseClient.sql("DELETE FROM product_status_likes").fetch().rowsUpdated().block();
        databaseClient.sql("DELETE FROM products").fetch().rowsUpdated().block();
    }

    @Test
    void findByProductIdAndUserIdShouldReturnLikeForUserAndProduct() {
        Mono<ProductStatusLikes> like = productStatusLikesRepository.findByProductIdAndUserId(1L, 1L);

        StepVerifier.create(like)
            .expectNextMatches(l -> l.getId().equals(1L))
            .verifyComplete();
    }

    @Test
    void findByUserIdShouldReturnAllLikesForUser() {
        Flux<ProductStatusLikes> likes = productStatusLikesRepository.findByUserId(2L);

        StepVerifier.create(likes)
            .expectNextMatches(l -> l.getProductId().equals(2L))
            .verifyComplete();
    }
}
