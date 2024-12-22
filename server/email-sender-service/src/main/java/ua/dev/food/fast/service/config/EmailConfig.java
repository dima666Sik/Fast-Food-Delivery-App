package ua.dev.food.fast.service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import ua.dev.feign.clients.OrderFeignClient;

import java.util.Properties;

import static ua.dev.food.fast.service.util.MailConstants.*;

@Configuration
public class EmailConfig {
    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.username}")
    private String email;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.protocol}")
    private String protocol;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(email);
        mailSender.setPassword(password);
        mailSender.setProtocol(protocol);
        Properties props = mailSender.getJavaMailProperties();
        props.put(MAIL_SMTP_AUTH, true);
        props.put(MAIL_SMTP_STARTTLS_ENABLE, true);
        return mailSender;
    }
}
