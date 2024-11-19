package ua.dev.food.fast.service.domain.model.product;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table("product_reviews")
public class ProductReview {
    @Id
    private Long id;
    private String review;
    private Long productId;
    private Long userId;

}