package ua.dev.food.fast.service.domain.dto.response;

import lombok.Builder;

@Builder
public record ErrorResponse(String message) {
}