package ua.dev.food.fast.service.domain.model.product;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table("products")
public class Product {
    @Id
    private Long id;
    private String title;
    private BigDecimal price;
    private Long likes;
    private String image01;
    private String image02;
    private String image03;
    private String category;
    private String description;
}

