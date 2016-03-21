package security.shiro.facebook;

public class FacebookSettings {
    private static String secretCache;
    private static String redirectUrlCache;
    private static String appIdCache;

    public static String getSecret() {
        if(secretCache == null)
        {
            secretCache = System.getenv("FACEBOOK_CLUBBY_SECRET");
        }
        return secretCache ;
    }

    public static String getRedirectUrl() {

        if(redirectUrlCache == null)
        {
            redirectUrlCache = System.getenv("FACEBOOK_CLUBBY_REDIRECT_URL");
        }
        return redirectUrlCache ;
    }

    public static String getAppId() {
        if(appIdCache == null)
        {
            appIdCache = System.getenv("FACEBOOK_CLUBBY_APP_ID");
        }
        return appIdCache ;
    }
}
