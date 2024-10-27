package ua.dev.food.fast.service.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduledRemoveTokens {
    private static final Logger log = LoggerFactory.getLogger(ScheduledRemoveTokens.class);
    private final TokensStatusChangeService tokensStatusChangeService;

    @Scheduled(fixedRate = 5000000)
    public void reportCurrentTime() {
        tokensStatusChangeService.deleteUserTokens()
                                 .then(tokensStatusChangeService.deleteUserRefreshTokens())
                                 .doOnSuccess(aVoid -> log.info("The expired and revoked tokens were removed."))
                                 .subscribe();  // You must subscribe to trigger reactive execution
    }
}
