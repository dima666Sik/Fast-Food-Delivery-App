package ua.dev.food.fast.service.gpt.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record Message(
    @JsonProperty("role") String role,
    @JsonProperty("content") String content) {
}