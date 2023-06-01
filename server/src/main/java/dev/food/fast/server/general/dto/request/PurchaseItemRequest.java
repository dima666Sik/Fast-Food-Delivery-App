package dev.food.fast.server.general.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseItemRequest {
    private Long id;
    private Double totalPrice;
    private Integer quantity;
}
