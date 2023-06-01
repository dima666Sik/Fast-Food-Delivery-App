package dev.food.fast.server.general.dto.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductStatusRequest {
    @JsonProperty("product_id")
    private Long idProduct;
    private Boolean status;
}
