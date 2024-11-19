package ua.dev.food.fast.service.domain.dto.request;


import com.fasterxml.jackson.annotation.JsonProperty;

public record ProductStatusRequestDto(
    @JsonProperty("product_id") Long idProduct,
    Boolean status
) {
}