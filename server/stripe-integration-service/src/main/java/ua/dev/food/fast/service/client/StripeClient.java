package ua.dev.food.fast.service.client;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.web.bind.annotation.RequestHeader;
import ua.dev.food.fast.service.exception.PaymentProcessingException;

import java.util.HashMap;
import java.util.Map;

@Component
@Log4j2
public class StripeClient {
    @Value("${stripe.secret-key}")
    private String secretKey;
    @Value("${stripe.publishable-key}")
    private String publishableKey;

    /**
     * Initializes the Stripe API key.
     * This method is called after the bean has been constructed and dependencies injected.
     */
    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }

    /**
     * Creates a PaymentIntent using the Stripe API.
     *
     * @param request A Map containing the payment details. It must include an "amount" key
     *                with a value that can be parsed to a long.
     * @return A Map containing the client_secret for the created PaymentIntent.
     * @throws PaymentProcessingException If there's an error while creating the PaymentIntent with Stripe.
     * @throws RuntimeException           If an unexpected error occurs during the process.
     */
    public Map<String, String> createPaymentIntent(Map<String, Object> request) {
        long amount = Long.parseLong(request.get("amount").toString());
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
            .setAmount(amount * 100)
            .setCurrency("usd")
            .build();

        PaymentIntent paymentIntent;
        try {
            paymentIntent = PaymentIntent.create(params);
        } catch (StripeException stripeException) {
            // Log and rethrow a specific exception for Stripe-related issues
            log.error("Stripe API error while creating PaymentIntent: {}", stripeException.getMessage(), stripeException);
            throw new PaymentProcessingException("Failed to create PaymentIntent due to Stripe error", stripeException);
        } catch (Exception genericException) {
            // Log and rethrow for unrelated generic exceptions
            log.error("Unexpected error occurred: {}", genericException.getMessage(), genericException);
            throw new RuntimeException("Unexpected error while processing payment", genericException);
        }


        Map<String, String> response = new HashMap<>();
        response.put("client_secret", paymentIntent.getClientSecret());
        return response;
    }

    public Map<String, String> getPublishableKey() {
        Map<String, String> response = new HashMap<>();
        response.put("publishableKey", publishableKey);
        return response;
    }
}
