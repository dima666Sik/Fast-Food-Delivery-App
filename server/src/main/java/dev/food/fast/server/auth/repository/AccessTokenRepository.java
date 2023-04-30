package dev.food.fast.server.auth.repository;

import java.util.List;
import java.util.Optional;

import dev.food.fast.server.auth.models.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AccessTokenRepository extends JpaRepository<AccessToken, Integer> {

  @Query(value = """
      select t from AccessToken t inner join User u\s
      on t.user.id = u.id\s
      where u.id = :id and (t.expired = false or t.revoked = false)\s
      """)
  List<AccessToken> findAllValidTokenByUser(Integer id);

  Optional<AccessToken> findByToken(String token);

  @Query(value = """
      select t from AccessToken t inner join User u\s
      on t.user.id = u.id\s
      where u.id = :id and (t.expired = true or t.revoked = true)\s
      """)
  List<AccessToken> deleteAllExpiredAndRevokedTokensByUser(Integer id);
}
