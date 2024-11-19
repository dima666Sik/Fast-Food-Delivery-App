package ua.dev.food.fast.service.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ua.dev.food.fast.service.domain.model.*;

import java.util.List;

@DataR2dbcTest
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = {PostgreSQLContainerInitializer.class})
class RefreshTokenRepositoryTest {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private UserRepository userRepository;
    private RefreshToken validToken;
    private RefreshToken expiredToken;
    private RefreshToken revokedToken;
    private User user;

    @BeforeEach
    public void setUp() {
        // Initialize sample data
        user = User.builder().email("lg@gmail.com").firstName("Dima").lastName("M")
            .permissions(List.of(Permission.builder().role(Role.USER).build())).password("****").build();
        user = userRepository.save(user).block();

        validToken = RefreshToken.builder().token("validToken123").tokenType(TokenType.BEARER).isExpired(false)
            .isRevoked(false).userId(user.getId()).build();
        expiredToken = RefreshToken.builder().token("expiredToken123").tokenType(TokenType.BEARER).isExpired(true)
            .isRevoked(false).userId(user.getId()).build();
        revokedToken = RefreshToken.builder().token("revokedToken123").tokenType(TokenType.BEARER).isExpired(false)
            .isRevoked(true).userId(user.getId()).build();

        // Save tokens to the database before each test
        validToken = refreshTokenRepository.save(validToken).block();
        expiredToken = refreshTokenRepository.save(expiredToken).block();
        revokedToken = refreshTokenRepository.save(revokedToken).block();
    }

    @AfterEach
    void tearDown() {
        refreshTokenRepository.deleteAllById(List.of(validToken.getId(), expiredToken.getId(), revokedToken.getId()))
            .block();
        userRepository.deleteById(user.getId()).block();
    }

    @Test
    void testFindValidRefreshTokenByUserId() {
        Mono<RefreshToken> result = refreshTokenRepository.findValidRefreshTokenByUserId(validToken.getUserId());

        StepVerifier.create(result)
            .expectNextMatches(token -> !token.isExpired() && !token.isRevoked() && token.getUserId()
                .equals(validToken.getUserId())).verifyComplete();
    }

    @Test
    void testFindAllExpiredAndRevokedRefreshTokens() {
        Flux<RefreshToken> result = refreshTokenRepository.findAllExpiredAndRevokedRefreshTokens();

        StepVerifier.create(result).expectNextMatches(token -> token.isExpired() || token.isRevoked())
            .expectNextCount(1) // Assuming we have exactly two tokens (expired and revoked)
            .verifyComplete();
    }
}
