package dev.food.fast.server.general.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseItemResponse {
    private Long id;
    private String image01;
    private String title;
    private Double price;
    private Integer quantity;
    private Double totalPrice;
}
