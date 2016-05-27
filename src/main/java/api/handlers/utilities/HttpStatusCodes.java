package api.handlers.utilities;

public class HttpStatusCodes {
    /* Successful 2xx */
    public static final int OK = 200;

    /* Client Error 4xx */
    public static final int BAD_REQUEST = 400;
    public static final int UNAUTHORIZED = 401;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;

    /* Server Error 5xx */
    public static final int INTERNAL_SERVER_ERROR = 500;
}
