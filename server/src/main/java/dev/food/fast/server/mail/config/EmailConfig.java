package dev.food.fast.server.mail.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {
    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.email}")
    private String email;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.protocol}")
    private String protocol;

    @Bean
    public JavaMailSender javaMailSender() {
        System.out.println("0000000000000000000000000000000000000000000000000000000000000000000000000EmailConfig|javaMailSender");
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(email);
        mailSender.setPassword(password);
        mailSender.setProtocol(protocol);
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.starttls.enable", true);
        return mailSender;
    }
}
