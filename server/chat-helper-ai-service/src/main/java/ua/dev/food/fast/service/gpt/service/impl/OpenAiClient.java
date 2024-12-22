package ua.dev.food.fast.service.gpt.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ua.dev.food.fast.service.gpt.domain.MessageHistory;
import ua.dev.food.fast.service.gpt.domain.dto.request.OpenAiRequest;
import ua.dev.food.fast.service.gpt.domain.dto.response.OpenAiResponse;

import java.util.List;
import java.util.Map;

/**
 * A client for interacting with the OpenAI API to generate AI responses based on input message history.
 *
 * <p>This class is configured using properties defined in the application's configuration file.</p>
 *
 * <ul>
 *     <li><b>apiKey</b>: The API key for authenticating with OpenAI.</li>
 *     <li><b>model</b>: The model name to use for generating responses.</li>
 *     <li><b>temperature</b>: Controls randomness in the response.</li>
 *     <li><b>maxTokens</b>: Maximum number of tokens in the generated response.</li>
 *     <li><b>topP</b>: Probability mass for nucleus sampling.</li>
 *     <li><b>frequencyPenalty</b>: Penalizes repeated tokens based on frequency.</li>
 *     <li><b>presencePenalty</b>: Penalizes repeated tokens based on presence.</li>
 * </ul>
 */

@RequiredArgsConstructor
@Component
public class OpenAiClient {

    private final WebClient webClient;

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.model}")
    private String model;

    @Value("${openai.api.temperature}")
    private Double temperature;

    @Value("${openai.api.max-tokens}")
    private Integer maxTokens;

    @Value("${openai.api.top-p}")
    private Double topP;

    @Value("${openai.api.frequency-penalty}")
    private Double frequencyPenalty;

    @Value("${openai.api.presence-penalty}")
    private Double presencePenalty;

    public Mono<OpenAiResponse> generateResponse(MessageHistory history) {
        System.out.println(history.messages());
        OpenAiRequest request = OpenAiRequest.builder().model(model).messages(history.messages())
            .temperature(temperature)
            .maxTokens(maxTokens)
            .topP(topP)
            .frequencyPenalty(frequencyPenalty)
            .presencePenalty(presencePenalty)
            .build();

        return webClient.post()
            .uri("/v1/chat/completions")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
            .bodyValue(request)
            .retrieve()
            .bodyToMono(OpenAiResponse.class);
    }
}