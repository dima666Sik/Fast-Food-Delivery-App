package ua.dev.food.fast.service.gpt.repository;

import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import ua.dev.food.fast.service.gpt.domain.MessageHistory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class MessageHistoryRepository {

    private final Map<Long, MessageHistory> storage = new ConcurrentHashMap<>();

    public Mono<MessageHistory> getHistory(Long userId) {
        return Mono.justOrEmpty(storage.get(userId));
    }

    public Mono<Void> saveHistory(MessageHistory history) {
        storage.put(history.userId(), history);
        return Mono.empty();
    }
}
