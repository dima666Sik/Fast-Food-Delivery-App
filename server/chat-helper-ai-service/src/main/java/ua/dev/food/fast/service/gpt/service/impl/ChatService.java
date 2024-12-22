package ua.dev.food.fast.service.gpt.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ua.dev.food.fast.service.gpt.domain.MessageHistory;
import ua.dev.food.fast.service.gpt.domain.dto.response.ChatResponse;
import ua.dev.food.fast.service.gpt.domain.dto.response.Message;
import ua.dev.food.fast.service.gpt.repository.MessageHistoryRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class ChatService {
    public static final String SYSTEM_THEMATIC_ASSISTANT = """
        You are interacting with an assistant for a fast food website.
        The site offers a variety of food options including sushi, burgers, and pizzas.
        The assistant can only help with navigation on site.
        Write shortly and without less text.
        At the top of the site there is a menu where you can choose what you need, for example, view all available food.
        Complete an order, contact technical support, log in as a user and chat with the AI chat.
        On the "Home" page, you can view the food and select the category you are interested in.
        Also, by clicking on a product, you can view it in detail and leave a review.
        On the "Food" page, you can sort food by the desired criteria and also search.
        On the "Cart" page, you can view the selected products and order them.
        On the "Contact" page, you can contact technical support.
                """;
    public static final int MAX_LENGTH_MESSAGE_HISTORY = 4;

    private final OpenAiClient openAiClient;
    private final MessageHistoryRepository historyRepository;

    public Mono<MessageHistory> getChatHistory(Long userId) {
        return historyRepository.getHistory(userId);
    }

    public Mono<ChatResponse> processMessage(Long userId, String message) {
        return historyRepository.getHistory(userId)
            .doOnNext(history -> log.info("Retrieved user history: {}, with message: {}", history, message))
            .defaultIfEmpty(new MessageHistory(userId))
            .doOnNext(history -> log.info("Initialized new history for user: {}", userId)).flatMap(history -> {
                if (history.messages().isEmpty()) {
                    history.addMessage(new Message("system", SYSTEM_THEMATIC_ASSISTANT));
                    log.info("Added system message to history: {}", history);
                }
                return Mono.just(history);
            }).doOnNext(history -> log.info("Updated history before sending to OpenAI: {}", history))
            .flatMap(history -> {
                history.addMessage(new Message("user", message));

                MessageHistory tempMessageHistoryForOpenAiClient = MessageHistory.builder().userId(userId)
                    .messages(new ArrayList<>()).build();
                List<Message> tempMessageHistory = new ArrayList<>();
                if (history.messages().size() <= MAX_LENGTH_MESSAGE_HISTORY)
                    tempMessageHistory.addAll(history.messages());
                else {
                    tempMessageHistory.add(history.messages().get(0));
                    tempMessageHistory.addAll(history.messages()
                        .subList(history.messages().size() - MAX_LENGTH_MESSAGE_HISTORY + 1, history.messages()
                            .size()));
                }

                tempMessageHistoryForOpenAiClient.addAllMessages(tempMessageHistory);

                log.debug("Added user message to temp history: {}", history);

                return openAiClient.generateResponse(tempMessageHistoryForOpenAiClient)
                    .doOnNext(response -> log.info("Received response from OpenAI: {}", response)).flatMap(response -> {
                        String assistantMessage = response.choices().get(0).message().content();
                        history.addMessage(new Message("assistant", assistantMessage));
                        return historyRepository.saveHistory(history)
                            .doOnSuccess(unused -> log.info("Saved history for user: {}", userId))
                            .thenReturn(new ChatResponse(assistantMessage));
                    });
            });
    }
}