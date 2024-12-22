package ua.dev.food.fast.service.gpt.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ua.dev.food.fast.service.gpt.domain.MessageHistory;
import ua.dev.food.fast.service.gpt.domain.dto.request.ChatRequest;
import ua.dev.food.fast.service.gpt.domain.dto.response.ChatResponse;
import ua.dev.food.fast.service.gpt.service.impl.ChatService;

/***
 * Controller for handling chat-related endpoints in the API.
 * Provides endpoints for sending messages and retrieving chat history.
 */
@RestController
@RequestMapping("/api/v2/chat-ai-helper")
@RequiredArgsConstructor
@Log4j2
public class ChatController {
    private final ChatService chatService;

    @PostMapping("/message")
    public Mono<ResponseEntity<ChatResponse>> sendMessage(@RequestBody ChatRequest chatRequest) {
        return chatService.processMessage(chatRequest.userId(), chatRequest.message())
            .map(ResponseEntity::ok)
            .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()));
    }

    @GetMapping("/get-chat-history")
    public Mono<ResponseEntity<MessageHistory>> getChatHistory(@RequestParam("user_id") Long userId) {
        return chatService.getChatHistory(userId)
            .map(ResponseEntity::ok)
            .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()));
    }
}