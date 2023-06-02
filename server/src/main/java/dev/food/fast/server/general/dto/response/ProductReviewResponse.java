package dev.food.fast.server.general.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductReviewResponse {
    private Long id;
    @JsonProperty("product_id")
    private Long idProduct;
    @JsonProperty("user_id")
    private Long idUser;
    @JsonProperty("first_name")
    private String firstname;
    @JsonProperty("last_name")
    private String lastname;
    private String email;
    private String review;
}



