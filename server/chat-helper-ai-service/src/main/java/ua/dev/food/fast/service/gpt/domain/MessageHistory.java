package ua.dev.food.fast.service.gpt.domain;

import lombok.Builder;
import ua.dev.food.fast.service.gpt.domain.dto.response.Message;

import java.util.ArrayList;
import java.util.List;

@Builder
public record MessageHistory(Long userId, List<Message> messages) {
    public MessageHistory(Long userId) {
        this(userId, new ArrayList<>());
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    public void addAllMessages(List<Message> messages) {
        this.messages.addAll(messages);
    }
}