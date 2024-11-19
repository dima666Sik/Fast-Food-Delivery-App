package ua.dev.food.fast.service.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
public record ProductReviewResponseDto(
    Long id,
    @JsonProperty("product_id") Long productId,
    @JsonProperty("user_id") Long userId,
    @JsonProperty("first_name") String firstNameReviewer,
    @JsonProperty("last_name") String lastNameReviewer,
    @JsonProperty("email") String emailReviewer,
    String review
) {
}



