package ua.dev.food.fast.service.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
public record ProductStatusLikesResponseDto(
    @JsonProperty("id")
    Long idProduct,
    Boolean status,
    Long likes) {
}
