package dev.food.fast.server.general.repository;

import dev.food.fast.server.general.dto.ProductReviewDTO;
import dev.food.fast.server.general.models.product.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductReviewRepository extends JpaRepository<ProductReview, Integer> {
    @Query("SELECT new dev.food.fast.server.general.dto.ProductReviewDTO(pr.id, pr.product.id, pr.user.id, u.firstname, u.lastname, u.email, pr.review) FROM ProductReview pr JOIN pr.user u WHERE pr.product.id = :product_id ORDER BY pr.id")
    List<ProductReviewDTO> findDTOByProduct_Id(@Param("product_id") Integer productId);

}