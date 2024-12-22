package ua.dev.food.fast.service.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import reactor.core.publisher.Mono;
import ua.dev.feign.clients.OrderFeignClient;
import ua.dev.feign.clients.OrderInfoRequestDto;
import ua.dev.feign.clients.ProductPurchaseDto;
import ua.dev.food.fast.service.domain.dto.request.EmailRequest;
import ua.dev.food.fast.service.exception_handler.exception.MessageSendingException;
import ua.dev.food.fast.service.util.MailConstants;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Log4j2
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final OrderFeignClient orderFeignClient;

    @Value("${spring.mail.username}")
    private String email;

    @Value("${back-end.urls.url.to-imgs}")
    private String partURLtoImages;

    public Mono<Void> sendReviewEmail(EmailRequest emailMessage) {
        if (emailMessage.getFrom() == null || emailMessage.getMessage() == null) {
            return Mono.error(new NoSuchElementException(MailConstants.INVALID_EMAIL_REQUEST));
        }
        return Mono.fromCallable(() -> {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

                helper.setTo(email);
                helper.setSubject("Fast Food Review");

                // Set variables to pass to the template
                Context context = new Context();
                context.setVariable("messageBody", emailMessage.getMessage());
                context.setVariable("senderEmail", emailMessage.getFrom());
                context.setVariable("username", emailMessage.getUsername());

                // Process the HTML template with variables
                String htmlContent = templateEngine.process("review-email", context);
                helper.setText(htmlContent, true);

                return message;
            })
            .flatMap(message -> Mono.fromRunnable(() -> mailSender.send(message))) // Send the email
            .doOnError(e -> log.error("Failed to send email: {}", e.getMessage(), e))
            .onErrorMap(e -> new MessageSendingException(MailConstants.errorSendingEmail(emailMessage.getFrom())))
            .then();
    }

    @RabbitListener(queues = "email-queue")
    public Mono<Void> sendOrderOnEmailGuest(OrderInfoRequestDto orderInfoRequestDto) {
        return orderFeignClient.getGuestPurchases(orderInfoRequestDto.orderGuestId())
            .flatMapMany(response -> response.getBody())
            .collectList()
            .flatMap(purchaseList -> {
                try {
                    if (purchaseList.isEmpty())
                        return Mono.error(new MessageSendingException(MailConstants.THE_PURCHASE_LIST_IS_EMPTY));

                    MimeMessage message = mailSender.createMimeMessage();
                    MimeMessageHelper helper = new MimeMessageHelper(message, true);

                    helper.setTo(orderInfoRequestDto.email());
                    helper.setSubject(String.format("Your order, dear %s, your email: %s",
                        orderInfoRequestDto.firstName(), orderInfoRequestDto.email()));

                    String htmlContent = generateEmailContent(orderInfoRequestDto, purchaseList);
                    helper.setText(htmlContent, true);

                    addInlineImages(purchaseList, helper);

                    mailSender.send(message);
                } catch (MessagingException e) {
                    log.error("Failed to send email: {}", e.getMessage(), e);
                    return Mono.error(new MessageSendingException(MailConstants.errorSendingEmail(orderInfoRequestDto.email())));
                }
                return Mono.empty();
            })
            .doOnError(e -> log.error("Failed to send email: {}", e.getMessage()))
            .then();
    }

    private String generateEmailContent(OrderInfoRequestDto orderInfoRequestDto, List<ProductPurchaseDto> purchaseList) {
        Context context = new Context();
        context.setVariable("order", orderInfoRequestDto);
        context.setVariable("purchaseList", purchaseList);

        return templateEngine.process("order-email", context);
    }

    private void addInlineImages(List<ProductPurchaseDto> purchaseList, MimeMessageHelper helper) {
        purchaseList.forEach(purchase -> {
            try {
                String imagePath = partURLtoImages + purchase.image01();
                FileSystemResource imageResource = new FileSystemResource(imagePath);

                helper.addInline(purchase.image01(), imageResource);
            } catch (MessagingException e) {
                log.error("Failed to add inline image: {}", e.getMessage());
            }
        });
    }

}