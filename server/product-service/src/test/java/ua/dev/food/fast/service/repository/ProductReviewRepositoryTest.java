package ua.dev.food.fast.service.repository;

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
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ua.dev.feign.clients.UserDto;
import ua.dev.feign.clients.UserFeignClient;
import ua.dev.food.fast.service.domain.model.product.Product;
import ua.dev.food.fast.service.domain.model.product.ProductReview;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@DataR2dbcTest
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = {PostgreSQLContainerInitializer.class})
class ProductReviewRepositoryTest {
    @Autowired
    private ProductReviewRepository productReviewRepository;

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
                INSERT INTO product_reviews (id, review, product_id, user_id) VALUES
                (1,'Review 1', 1, 1),
                (2,'Review 2', 1, 2)
                """)
            .fetch().rowsUpdated().block();
    }

    @AfterEach
    void tearDown() {
        databaseClient.sql("DELETE FROM product_reviews").fetch().rowsUpdated().block();
        databaseClient.sql("DELETE FROM products").fetch().rowsUpdated().block();
    }

    @Test
    void findByProductIdOrderByProductIdAscShouldReturnReviewsForProduct() {
        Flux<ProductReview> reviews = productReviewRepository.findByProductIdOrderByProductIdAsc(1L);

        StepVerifier.create(reviews)
            .expectNextMatches(review -> review.getUserId().equals(1L) && review.getReview().equals("Review 1"))
            .expectNextMatches(review -> review.getUserId().equals(2L) && review.getReview().equals("Review 2"))
            .verifyComplete();
    }

    @Test
    void findByIdAndProductIdAndUserIdShouldReturnSpecificReview() {
        Mono<ProductReview> review = productReviewRepository.findByIdAndProductIdAndUserId(1L, 1L, 1L);

        StepVerifier.create(review)
            .expectNextMatches(r -> r.getReview().equals("Review 1"))
            .verifyComplete();
    }
}