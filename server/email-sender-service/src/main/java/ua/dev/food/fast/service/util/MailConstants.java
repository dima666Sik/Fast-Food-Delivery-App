package ua.dev.food.fast.service.util;

public class MailConstants {
    public static final String INVALID_EMAIL_REQUEST = "Invalid email request";
    public static final String THE_PURCHASE_LIST_IS_EMPTY = "The purchase list is empty";

    private MailConstants() {
    }

    public static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
    public static final String MAIL_SMTP_STARTTLS_ENABLE = "mail.smtp.starttls.enable";

    public static String errorSendingEmail(String email) {
        return "Error sending from email: " + email;
    }

}
