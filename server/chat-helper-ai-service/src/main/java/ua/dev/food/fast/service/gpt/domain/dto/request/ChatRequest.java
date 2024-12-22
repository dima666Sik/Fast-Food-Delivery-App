package ua.dev.food.fast.service.gpt.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

// Модели для работы с запросами и ответами
@Builder
public record ChatRequest(@JsonProperty("user_id") Long userId, String message) {
}