package ua.dev.food.fast.service.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class RabbitMQConsumerConfig {
    @Value("${spring.rabbitmq.host}")
    private String rabbitHost;

    @Value("${spring.rabbitmq.username}")
    private String rabbitUsername;

    @Value("${spring.rabbitmq.password}")
    private String rabbitPassword;

    @Value("${spring.rabbitmq.virtual-host}")
    private String virtualHost;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(rabbitHost);
        connectionFactory.setUsername(rabbitUsername);
        connectionFactory.setPassword(rabbitPassword);
        connectionFactory.setVirtualHost(virtualHost);
        connectionFactory.setRequestedHeartBeat(30);
        return connectionFactory;
    }

    @Bean
    public Queue emailQueue() {
        return new Queue("email-queue", true);
    }

    @Bean
    public Queue supportEmailQueue() {
        return new Queue("support-email-queue", true);
    }

    @Bean
    public MessageConverter messageConverter() {
        SimpleMessageConverter converter = new SimpleMessageConverter();
        converter.setAllowedListPatterns(List.of("ua.dev.feign.clients.OrderInfoRequestDto",
            "ua.dev.food.fast.service.domain.dto.request.EmailRequest",
            "java.lang.Boolean",
            "java.math.BigDecimal",
            "java.lang.String"
        ));
        return converter;
    }
}
