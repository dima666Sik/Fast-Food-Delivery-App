package ua.dev.food.fast.service.domain.dto.request;

import lombok.Builder;
import lombok.NonNull;

import java.math.BigDecimal;

@Builder
public record ProductRequestDto(@NonNull String title, @NonNull String category, @NonNull String description,
                                @NonNull BigDecimal price) {
}