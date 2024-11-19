package ua.dev.food.fast.service.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduledRemoveTokensService {
    private static final Logger log = LoggerFactory.getLogger(ScheduledRemoveTokensService.class);
    private final TokensHandlingService tokensHandlingService;

    @Scheduled(fixedRate = 5000000)
    public void reportCurrentTime() {
        tokensHandlingService.deleteUserTokens()
            .then(tokensHandlingService.deleteUserRefreshTokens())
            .doOnSuccess(aVoid -> log.info("The expired and revoked tokens were removed."))
            .subscribe();
    }
}
