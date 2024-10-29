package ua.dev.food.fast.service.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.dev.food.fast.service.domain.model.RefreshToken;

public interface RefreshTokenRepository extends ReactiveCrudRepository<RefreshToken, Long> {

    @Query(value = """
        select id, token, token_type, is_revoked, is_expired, user_id from refresh_tokens where user_id = :user_id and is_expired = false and is_revoked = false
        """)
    Mono<RefreshToken> findValidRefreshTokenByUserId(@Param("user_id") Long userId);

    @Query(value = """
           select id, token, token_type, is_revoked, is_expired, user_id from refresh_tokens where is_expired = true or is_revoked = true
        """)
    Flux<RefreshToken> findAllExpiredAndRevokedRefreshTokens();


}
