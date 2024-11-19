package ua.dev.food.fast.service.domain.model.product;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "product_status_likes")
public class ProductStatusLikes {

    @Id
    private Long id;
    private Boolean status;
    private Long productId;
    private Long userId;
}
