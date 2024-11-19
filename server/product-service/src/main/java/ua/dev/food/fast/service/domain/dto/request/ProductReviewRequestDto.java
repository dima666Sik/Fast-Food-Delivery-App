package ua.dev.food.fast.service.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ProductReviewRequestDto(
    @JsonProperty("product_id") Long idProduct,
    String review
) {
}