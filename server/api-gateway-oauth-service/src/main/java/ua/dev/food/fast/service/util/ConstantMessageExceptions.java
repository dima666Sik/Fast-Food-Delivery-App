package ua.dev.food.fast.service.util;

public final class ConstantMessageExceptions {

    public static final String BEARER_HEADER = "Bearer ";
    public static final String INTERNAL_SERVER_ERROR = "Internal Server Error";
    ;

    private ConstantMessageExceptions() {
        // Private constructor to prevent instantiation
    }

    public static final String UNSUCCESSFUL_LOGOUT = "Unsuccessful logout";
    public static final String TOKENS_WERE_REFRESHED = "Tokens were refreshed";
    public static final String ACCESS_TOKEN_HAS_EXPIRED = "Access token has expired";
    public static final String ACCESS_TOKEN_HAS_REVOKED = "Access token has revoked";
    public static final String ACCESS_REFRESH_TOKENS_HAVE_EXPIRED = "Access & Refresh tokens have expired";
    public static final String AUTHORIZATION_HEADER_IS_EMPTY = "Authorization header is empty!";
    public static final String INVALID_TOKEN = "Invalid token";
    public static final String ACCESS_TOKEN_NOT_FOUND = "Access token not found";
    public static final String USER_NOT_FOUND = "User not found";
    public static final String REFRESH_TOKEN_NOT_FOUND = "Refresh token not found";
    public static final String INCORRECT_EMAIL = "Incorrect email. User not found";
    public static final String INCORRECT_PASSWORD = "Incorrect password";

    public static String userAlreadyExists(String email) {
        return "User with email " + email + " already exists";
    }

}
