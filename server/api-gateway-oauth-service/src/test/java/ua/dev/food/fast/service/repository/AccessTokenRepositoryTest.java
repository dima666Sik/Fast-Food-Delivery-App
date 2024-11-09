package ua.dev.food.fast.service.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import ua.dev.food.fast.service.domain.model.*;
import ua.dev.food.fast.service.service.JwtService;

import java.util.List;

@DataR2dbcTest
@Testcontainers
@ActiveProfiles("test")
class AccessTokenRepositoryTest {
    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:12")
        .withDatabaseName("test_db")
        .withUsername("test")
        .withPassword("test");

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
    private AccessTokenRepository accessTokenRepository;
    @Autowired
    private UserRepository userRepository;

    @MockBean
    private JwtService jwtService;
    private AccessToken token;
    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder().email("lg@gmail.com").firstname("Dima").lastname("M")
            .permissions(List.of(Permission.builder().role(Role.USER).build())).password("****").build();
        user = userRepository.save(user).block();

        token = creationToken(false, false);
        token = accessTokenRepository.save(token).block();
    }

    @AfterEach
    void tearDown() {
        accessTokenRepository.deleteById(token.getId()).block();
        userRepository.deleteById(user.getId()).block();
    }

    private AccessToken creationToken(boolean expired, boolean revoke) {
        String mockedToken = "mocked_token_value";
        Mockito.when(jwtService.generateToken(user)).thenReturn(mockedToken);

        return AccessToken.builder().token(mockedToken).tokenType(TokenType.BEARER).isExpired(expired)
            .isRevoked(revoke).userId(user.getId())
            .build();
    }

    @Test
    void testFindValidAccessTokenByUserId() {
        StepVerifier.create(accessTokenRepository.findValidAccessTokenByUserId(token.getUserId()))
            .expectNextMatches(accessToken -> !accessToken.isExpired() && !accessToken.isRevoked())
            .verifyComplete();
    }

    @Test
    void testFindByToken() {
        String receivedToken = token.getToken();
        StepVerifier.create(accessTokenRepository.findByToken(token.getToken()))
            .expectNextMatches(accessToken -> receivedToken.equals(accessToken.getToken()))
            .verifyComplete();
    }

    @Test
    void testFindAllExpiredAndRevokedTokens() {
        AccessToken expiredToken = creationToken(true, false);
        AccessToken revokedToken = creationToken(false, true);

        accessTokenRepository.saveAll(Flux.just(expiredToken, revokedToken)).blockLast();

        StepVerifier.create(accessTokenRepository.findAllExpiredAndRevokedTokens())
            .expectNextCount(2)
            .verifyComplete();
    }
}