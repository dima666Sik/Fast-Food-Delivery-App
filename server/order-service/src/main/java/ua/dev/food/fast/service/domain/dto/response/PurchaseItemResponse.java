package ua.dev.food.fast.service.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseItemResponse {
    private Long id;
    private String image01;
    private String title;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal totalPrice;
}
