package ua.dev.food.fast.service.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseItemRequest {
    @JsonProperty("id")
    private Long idProduct;
    private BigDecimal totalPrice;
    private Integer quantity;
}

