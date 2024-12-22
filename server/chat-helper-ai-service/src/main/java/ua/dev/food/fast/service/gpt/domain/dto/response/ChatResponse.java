package ua.dev.food.fast.service.gpt.domain.dto.response;

import lombok.Builder;

@Builder
public record ChatResponse(String message) {
}