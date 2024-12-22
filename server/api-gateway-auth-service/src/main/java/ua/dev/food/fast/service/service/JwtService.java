package ua.dev.food.fast.service.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ua.dev.food.fast.service.domain.model.User;
import ua.dev.jwt.service.JwtDecodeService;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/***
 * Service for handling JSON Web Tokens (JWT), including generation and validation of access and refresh tokens.
 */
@Service
@RequiredArgsConstructor
public class JwtService extends JwtDecodeService {

    /**
     * The expiration time for JWT access tokens, configured via application properties.
     */
    @Value("${back-end.security.jwt.access.expiration}")
    private long jwtExpiration;

    /**
     * The expiration time for JWT refresh tokens, configured via application properties.
     */
    @Value("${back-end.security.jwt.refresh.expiration}")
    private long refreshExpiration;

    /**
     * Generates a JWT access token with additional claims extracted from the provided user details.
     *
     * @param userDetails The user details used for generating the token.
     * @return A signed JWT access token as a string.
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> extraClaims = new HashMap<>();
        if (userDetails instanceof User user) {
            extraClaims.put("user_id", user.getId());
            extraClaims.put("roles", user.getPermissions()
                .stream()
                .map(permission -> permission.getRole().name())
                .toList());
            extraClaims.put("first_name", user.getFirstName());
            extraClaims.put("last_name", user.getLastName());
        }
        return generateToken(extraClaims, userDetails);
    }

    /**
     * Generates a JWT access token with custom claims and expiration.
     *
     * @param extraClaims Custom claims to include in the token.
     * @param userDetails The user details used for generating the token.
     * @return A signed JWT access token as a string.
     */
    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    /**
     * Generates a JWT refresh token for the provided user details.
     *
     * @param userDetails The user details used for generating the refresh token.
     * @return A signed JWT refresh token as a string.
     */
    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, refreshExpiration);
    }

    /**
     * Builds a JWT token with specified claims, subject, and expiration.
     *
     * @param extraClaims Custom claims to include in the token.
     * @param userDetails The user details used as the token subject.
     * @param expiration  The expiration time in milliseconds for the token.
     * @return A signed JWT token as a string.
     */
    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
        return Jwts.builder()
            .setClaims(extraClaims)
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    /**
     * Validates the provided token by checking the username and expiration.
     *
     * @param token       The JWT token to validate.
     * @param userDetails The user details to compare against the token's subject.
     * @return True if the token is valid; false otherwise.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Checks if the provided token is expired.
     *
     * @param token The JWT token to check.
     * @return True if the token is expired; false otherwise.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts the expiration date from the provided token.
     *
     * @param token The JWT token to parse.
     * @return The expiration date of the token.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}

