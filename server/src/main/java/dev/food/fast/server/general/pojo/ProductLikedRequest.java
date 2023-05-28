package dev.food.fast.server.general.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class ProductLikedRequest {
    @JsonProperty("product_id")
    private Integer idProduct;
    private Integer likes;

}
