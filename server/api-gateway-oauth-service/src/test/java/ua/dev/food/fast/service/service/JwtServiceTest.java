package ua.dev.food.fast.service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import ua.dev.food.fast.service.domain.model.Permission;
import ua.dev.food.fast.service.domain.model.Role;
import ua.dev.food.fast.service.domain.model.User;
import ua.dev.jwt.service.JwtDecodeService;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class JwtServiceTest {

    @Autowired
    private JwtService jwtService;
    private User user;

    @Value("${back-end.security.jwt.access.expiration}")
    private long jwtExpiration;
    @Value("${back-end.security.jwt.refresh.expiration}")
    private long refreshExpiration;

    @BeforeEach
    void setUp() throws Exception {
        setField(jwtService, "jwtExpiration", jwtExpiration);
        setField(jwtService, "refreshExpiration", refreshExpiration);

        user = User.builder().email("lg@gmail.com").firstname("Dima").lastname("M")
            .permissions(List.of(Permission.builder().role(Role.USER).build())).password("****").build();
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    void testGenerateToken() {
        String token = jwtService.generateToken(user);
        assertNotNull(token);
        assertTrue(jwtService.isTokenValid(token, user));
        assertEquals(user.getEmail(), jwtService.extractUsername(token));
    }

    @Test
    void testIsTokenValid() {
        String token = jwtService.generateToken(user);
        assertTrue(jwtService.isTokenValid(token, user));
    }

    @Test
    void testExtractUsername() {
        String token = jwtService.generateToken(user);

        assertEquals(user.getEmail(), jwtService.extractUsername(token));
    }

    @Test
    void testGenerateRefreshToken() {
        String refreshToken = jwtService.generateRefreshToken(user);

        assertNotNull(refreshToken);
        assertTrue(jwtService.isTokenValid(refreshToken, user));
    }
}