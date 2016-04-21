package clients.facebook;

public class FacebookSettings {
    private static String secretCache;
    private static String redirectUrlCache;
    private static String appIdCache;
    private static String accessTokenKey = "FACEBOOK_ACCESS_TOKEN";

    public static String getSecret() {
        if (secretCache == null) {
            secretCache = System.getenv("FACEBOOK_CLUBBY_SECRET");
        }
        return secretCache;
    }

    public static String getRedirectUrl() {

        if (redirectUrlCache == null) {
            redirectUrlCache = System.getenv("FACEBOOK_CLUBBY_REDIRECT_URL");
        }
        return redirectUrlCache;
    }

    public static String getAppId() {
        if (appIdCache == null) {
            appIdCache = System.getenv("FACEBOOK_CLUBBY_APP_ID");
        }
        return appIdCache;
    }

    public static String getAccessTokenKey() {
        return accessTokenKey;
    }
}
