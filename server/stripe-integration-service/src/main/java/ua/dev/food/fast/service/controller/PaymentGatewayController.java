package ua.dev.food.fast.service.controller;

import com.stripe.model.Charge;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ua.dev.food.fast.service.client.StripeClient;

import java.util.Map;

@RestController
@RequestMapping("/api/v2/payment")
@RequiredArgsConstructor
public class PaymentGatewayController {
    private final StripeClient stripeClient;

    @GetMapping("/config")
    public Mono<ResponseEntity<Map<String, String>>> getConfig() {
        return Mono.fromCallable(() -> ResponseEntity.ok(stripeClient.getPublishableKey()));
    }

    @PostMapping("/create-payment-intent")
    public Mono<ResponseEntity<Map<String, String>>> createPaymentIntent(@RequestBody Map<String, Object> request) {
        return Mono.fromCallable(() -> ResponseEntity.ok(stripeClient.createPaymentIntent(request)));
    }
}
