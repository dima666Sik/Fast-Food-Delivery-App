package dev.food.fast.server.mail.service;

import dev.food.fast.server.auth.dto.response.MessageResponse;
import dev.food.fast.server.general.dto.request.OrderPurchaseRequest;
import dev.food.fast.server.general.models.order.Purchase;
import dev.food.fast.server.mail.dto.request.EmailRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.email}")
    private String email;

    @Value("${server.part.url.to.images}")
    private String partURLtoImages;

    public ResponseEntity<String> sendReviewEmail(EmailRequest emailMessage) {

        String from = emailMessage.getFrom();
        String username = emailMessage.getUsername();
        String body = emailMessage.getMessage();

        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(email);
            helper.setSubject("Fast Food Review");

            String htmlContent = "<html>" +
                    "<head><style>" +
                    "    body { font-family: Arial, sans-serif; font-size: 16px; color: #333; }" +
                    "    h1 { color: orangered; }" +
                    "    table { border-collapse: collapse; }" +
                    "    th, td { padding: 12px 16px; text-align: left; border-bottom: 1px solid #ddd; }" +
                    "    th { background-color: #f2f2f2; font-weight: bold; }" +
                    "    td { font-style: italic; }" +
                    "    td.email { font-weight: bold; color: orangered; }" +
                    "    td.username { font-weight: bold; }" +
                    "</style></head>" +
                    "<body>" +
                    "  <section>" +
                    "    <h1>Hello!</h1>" +
                    "    <p>This email was sent by a client. Please read any comments or suggestions! :)</p>" +
                    "    <div class='table-container'>" +
                    "      <table>" +
                    "        <tr>" +
                    "          <th>Email content:</th>" +
                    "          <td>" + body + "</td>" +
                    "        </tr>" +
                    "        <tr>" +
                    "          <th>Email sender:</th>" +
                    "          <td class='email'>" + from + "</td>" +
                    "        </tr>" +
                    "        <tr>" +
                    "          <th>Best regards:</th>" +
                    "          <td class='username'>" + username + "</td>" +
                    "        </tr>" +
                    "      </table>" +
                    "    </div>" +
                    "  </section>" +
                    "</body>" +
                    "</html>";

            helper.setText(htmlContent, true);
            mailSender.send(message);
            return ResponseEntity.ok("Email sent successfully");
        } catch (MessagingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending email: " + e.getMessage());
        }
    }

    public ResponseEntity<?> sendOrderOnEmailGuest(OrderPurchaseRequest orderPurchaseRequest, Long idOrder, List<Purchase> purchaseList) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(orderPurchaseRequest.getEmail());
            helper.setSubject("Your order, dear " + orderPurchaseRequest.getName() + ", your email: " + orderPurchaseRequest.getEmail());

            String tableContent = generateProductTable(orderPurchaseRequest, idOrder, purchaseList, helper);

            helper.setText(tableContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            return ResponseEntity.ok().body(MessageResponse.builder()
                    .message("Failed to send email.")
                    .status(false)
                    .build());
        }

        return ResponseEntity.ok("Order was added successfully");
    }

    private String generateProductTable(OrderPurchaseRequest orderPurchaseRequest, Long idOrder,
                                        List<Purchase> purchaseList, MimeMessageHelper helper) {
        StringBuilder tableBuilder = new StringBuilder();

        tableBuilder.append("<style>")
                .append("table {")
                .append("  border-collapse: collapse;")
                .append("  width: 100%;")
                .append("}")
                .append("th, td {")
                .append("  text-align: left;")
                .append("  padding: 8px;")
                .append("}")
                .append("th {")
                .append("  background-color: #f2f2f2;")
                .append("}")
                .append("tr:nth-child(even) {")
                .append("  background-color: #f9f9f9;")
                .append("}")
                .append(".order-info {")
                .append("  text-align: center;")
                .append("  margin-bottom: 5px;")
                .append("  font-size: 1.5rem;")
                .append("}")
                .append("img {")
                .append("  width: 70px;")
                .append("}")
                .append("</style>");

        tableBuilder.append("<p class=\"order-info\"><b><strong>Order ID: </strong>").append(idOrder).append("</b></p>");
        tableBuilder.append("<p class=\"order-info\"><strong>Phone: </strong>").append(orderPurchaseRequest.getPhone()).append("</p>");
        tableBuilder.append("<p class=\"order-info\"><strong>Order Date: </strong>").append(orderPurchaseRequest.getDateArrived()).append("</p>");
        tableBuilder.append("<p class=\"order-info\"><strong>Order Time: </strong>").append(orderPurchaseRequest.getTimeArrived()).append("</p>");
        tableBuilder.append("<p class=\"order-info\"><strong>Total Order Price: </strong>").append(orderPurchaseRequest.getTotalAmount()).append("$").append("</p>");
        tableBuilder.append("<p class=\"order-info\"><strong>Delivery Type: </strong>").append(orderPurchaseRequest.getDelivery() ? "Yes" : "No").append("</p>");

        tableBuilder.append("<table>")
                .append("<tr>")
                .append("<th>Product Img</th>")
                .append("<th>Product Name</th>")
                .append("<th>Quantity</th>")
                .append("<th>Total Price</th>")
                .append("</tr>");

        for (Purchase purchase : purchaseList) {
            String imageName = purchase.getProduct().getImage01();
            String productName = purchase.getProduct().getTitle();
            int quantity = purchase.getQuantity();
            double totalPrice = purchase.getTotalPrice();

            String imagePath = "src/main/resources/static/public/images/products/" + imageName;
            try {
                String absolutePath = ResourceUtils.getFile(imagePath).getAbsolutePath();
                FileSystemResource photoFile = new FileSystemResource(absolutePath);
                helper.addInline(imageName, photoFile);
                tableBuilder.append("<tr>")
                        .append("<td>").append("<img src='cid:").append(imageName).append("'/>").append("</td>")
                        .append("<td>").append(productName).append("</td>")
                        .append("<td>").append(quantity).append("</td>")
                        .append("<td>").append(totalPrice).append("$").append("</td>")
                        .append("</tr>");
            } catch (MessagingException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        tableBuilder.append("</table>");

        return tableBuilder.toString();
    }
}