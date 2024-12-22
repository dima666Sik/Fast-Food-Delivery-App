package ua.dev.food.fast.service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQProducerConfig {
    @Bean
    public DirectExchange emailExchange() {
        return new DirectExchange("email-exchange");
    }

    @Bean
    public Queue emailQueue() {
        return new Queue("email-queue", true);
    }

    @Bean
    public Binding emailBinding() {
        return BindingBuilder.bind(emailQueue()).to(emailExchange()).with("email-routing-key");
    }
}
