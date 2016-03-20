package security.shiro.facebook;

public class FacebookSettings {
    // TODO: cache
    public static String getSecret() {
        return System.getenv("FACEBOOK_CLUBBY_SECRET");
    }

    public static String getRedirectUrl() {
        return System.getenv("FACEBOOK_CLUBBY_REDIRECT_URL");
    }

    public static String getAppId() {
        return System.getenv("FACEBOOK_CLUBBY_APP_ID");
    }
}
