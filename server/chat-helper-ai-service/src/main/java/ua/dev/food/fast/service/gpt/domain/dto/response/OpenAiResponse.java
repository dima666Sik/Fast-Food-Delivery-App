package ua.dev.food.fast.service.gpt.domain.dto.response;

import java.util.List;

public record OpenAiResponse(String id, String object, String created, String model, List<Choice> choices) {
}
