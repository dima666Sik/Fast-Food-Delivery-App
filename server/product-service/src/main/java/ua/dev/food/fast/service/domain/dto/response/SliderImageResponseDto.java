package ua.dev.food.fast.service.domain.dto.response;

import lombok.Builder;

@Builder
public record SliderImageResponseDto(
    Long id,
    String urlImg
) {
}
