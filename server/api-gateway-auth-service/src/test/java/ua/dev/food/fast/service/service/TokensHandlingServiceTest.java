package ua.dev.food.fast.service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ua.dev.food.fast.service.domain.model.AccessToken;
import ua.dev.food.fast.service.domain.model.RefreshToken;
import ua.dev.food.fast.service.domain.model.User;
import ua.dev.food.fast.service.repository.AccessTokenRepository;
import ua.dev.food.fast.service.repository.RefreshTokenRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class TokensHandlingServiceTest {
    @Mock
    private AccessTokenRepository accessTokenRepository;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @InjectMocks
    private TokensHandlingService tokensHandlingService;
    private User user;
    private AccessToken accessToken;
    private RefreshToken refreshToken;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        accessToken = new AccessToken();
        accessToken.setToken("accessJwtToken");
        accessToken.setUserId(user.getId());
        accessToken.setExpired(false);
        accessToken.setRevoked(false);

        refreshToken = new RefreshToken();
        refreshToken.setToken("refreshJwtToken");
        refreshToken.setUserId(user.getId());
        refreshToken.setExpired(false);
        refreshToken.setRevoked(false);
    }

    @Test
    void testExpireAllUserRefreshTokens() {
        when(refreshTokenRepository.findValidRefreshTokenByUserId(user.getId())).thenReturn(Mono.just(refreshToken));
        when(refreshTokenRepository.save(refreshToken)).thenReturn(Mono.just(refreshToken));

        StepVerifier.create(tokensHandlingService.expireAllUserRefreshTokens(user))
            .expectComplete()
            .verify();

        verify(refreshTokenRepository).save(any(RefreshToken.class));
        verify(refreshTokenRepository).findValidRefreshTokenByUserId(user.getId());
    }

    @Test
    void testExpireAllUserTokens() {
        when(accessTokenRepository.findValidAccessTokenByUserId(user.getId())).thenReturn(Mono.just(accessToken));
        when(accessTokenRepository.save(accessToken)).thenReturn(Mono.just(accessToken));

        StepVerifier.create(tokensHandlingService.expireAllUserTokens(user))
            .expectComplete()
            .verify();

        verify(accessTokenRepository).save(any(AccessToken.class));
        verify(accessTokenRepository).findValidAccessTokenByUserId(user.getId());
    }

    @Test
    void testDeleteUserTokens() {
        when(accessTokenRepository.findAllExpiredAndRevokedTokens()).thenReturn(Flux.just(accessToken));
        when(accessTokenRepository.deleteAll(anyList())).thenReturn(Mono.empty());

        StepVerifier.create(tokensHandlingService.deleteUserTokens())
            .expectComplete()
            .verify();

        verify(accessTokenRepository).deleteAll(anyList());
        verify(accessTokenRepository).findAllExpiredAndRevokedTokens();
    }

    @Test
    void testDeleteUserRefreshTokens() {
        when(refreshTokenRepository.findAllExpiredAndRevokedRefreshTokens()).thenReturn(Flux.just(refreshToken));
        when(refreshTokenRepository.deleteAll(anyList())).thenReturn(Mono.empty());

        StepVerifier.create(tokensHandlingService.deleteUserRefreshTokens())
            .expectComplete()
            .verify();

        verify(refreshTokenRepository).deleteAll(anyList());
        verify(refreshTokenRepository).findAllExpiredAndRevokedRefreshTokens();
    }

    @Test
    void testSaveUserToken() {
        String jwtToken = "accessJwtToken";
        when(accessTokenRepository.save(any(AccessToken.class))).thenReturn(Mono.just(accessToken));

        StepVerifier.create(tokensHandlingService.saveUserToken(user, jwtToken))
            .expectNextMatches(token -> token.getToken().equals(jwtToken))
            .verifyComplete();

        verify(accessTokenRepository).save(any(AccessToken.class));
    }

    @Test
    void testSaveUserRefreshToken() {
        String jwtToken = "refreshJwtToken";
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(Mono.just(refreshToken));

        StepVerifier.create(tokensHandlingService.saveUserRefreshToken(user, jwtToken))
            .expectNextMatches(token -> token.getToken().equals(jwtToken))
            .verifyComplete();

        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }

}