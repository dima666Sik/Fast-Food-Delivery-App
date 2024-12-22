package ua.dev.food.fast.service.gpt.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import ua.dev.food.fast.service.gpt.domain.dto.response.Message;

import java.util.List;

@Builder
public record OpenAiRequest(@JsonProperty("model") String model,
                            @JsonProperty("messages") List<Message> messages,
                            @JsonProperty("temperature") Double temperature,
                            @JsonProperty("max_tokens") Integer maxTokens,
                            @JsonProperty("top_p") Double topP,
                            @JsonProperty("presence_penalty") Double presencePenalty,
                            @JsonProperty("frequency_penalty") Double frequencyPenalty) {
}
