package ua.dev.feign.clients;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProductResponseDto(
    Long id,
    String title,
    BigDecimal price,
    Long likes,
    String image01,
    String image02,
    String image03,
    String category,
    String description
) {
}