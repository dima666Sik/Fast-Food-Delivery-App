package dev.food.fast.server.auth.repository;

import dev.food.fast.server.auth.models.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {

    @Query(value = """
      select t from RefreshToken t inner join User u\s
      on t.user.id = u.id\s
      where u.id = :id and (t.expired = false or t.revoked = false)\s
      """)
    Optional<RefreshToken> findValidRefreshTokenByUser(Integer id);

    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    @Query(value = """
      select t from RefreshToken t inner join User u\s
      on t.user.id = u.id\s
      where u.id = :id and (t.expired = true or t.revoked = true)\s
      """)
    List<RefreshToken> deleteAllExpiredAndRevokedRefreshTokensByUser(Integer id);


}
