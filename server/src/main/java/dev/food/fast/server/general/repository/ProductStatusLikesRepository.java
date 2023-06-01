package dev.food.fast.server.general.repository;

import dev.food.fast.server.general.dto.response.ProductStatusLikesResponse;
import dev.food.fast.server.general.models.product.ProductStatusLikes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductStatusLikesRepository extends JpaRepository<ProductStatusLikes, Integer> {
    Optional<ProductStatusLikes> findByProduct_IdAndUser_Id(Integer productId, Integer userId);

    @Query("SELECT new dev.food.fast.server.general.dto.response.ProductStatusLikesResponse(pl.product.id, pl.status, p.likes) FROM ProductStatusLikes pl JOIN pl.product p WHERE pl.user.id = :userId")
    List<ProductStatusLikesResponse> findResponseStatusLikedByUser_Id(@Param("userId") Integer userId);
}
