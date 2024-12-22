package ua.dev.food.fast.service.gpt.domain.dto.response;

public record Choice(
    int index,
    Message message,
    String finishReason
) {
}