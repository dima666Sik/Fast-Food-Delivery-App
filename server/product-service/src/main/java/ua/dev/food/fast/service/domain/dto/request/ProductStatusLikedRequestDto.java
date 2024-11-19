package ua.dev.food.fast.service.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ProductStatusLikedRequestDto(
    @JsonProperty("product_id") Long idProduct,
    Long likes
) {
}