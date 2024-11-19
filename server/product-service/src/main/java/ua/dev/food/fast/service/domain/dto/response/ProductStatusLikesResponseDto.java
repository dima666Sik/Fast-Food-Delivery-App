package ua.dev.food.fast.service.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
public record ProductStatusLikesResponseDto(
    Long id,
    Boolean status,
    Long likes) {
}
