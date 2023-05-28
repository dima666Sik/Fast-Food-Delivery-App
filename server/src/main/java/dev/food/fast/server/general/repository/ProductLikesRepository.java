package dev.food.fast.server.general.repository;

import dev.food.fast.server.general.dto.ProductLikesDTO;
import dev.food.fast.server.general.models.product.ProductLikes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductLikesRepository extends JpaRepository<ProductLikes, Integer> {
    Optional<ProductLikes> findByProduct_IdAndUser_Id(Integer productId, Integer userId);

    @Query("SELECT new dev.food.fast.server.general.dto.ProductLikesDTO(pl.product.id, pl.status, p.likes) FROM ProductLikes pl JOIN pl.product p WHERE pl.user.id = :userId")
    List<ProductLikesDTO> findDTOByUser_Id(@Param("userId") Integer userId);
}
