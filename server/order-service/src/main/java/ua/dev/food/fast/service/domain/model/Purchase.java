package ua.dev.food.fast.service.domain.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "purchase")
public class Purchase {
    @Id
    private Long id;
    private Long productId;
    private Long basicOrderUserId;
    private Long basicOrderGuestId;
    private BigDecimal totalPrice;
    private Integer quantity;
}
