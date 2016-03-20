package security.shiro.facebook;

import com.google.api.client.http.*;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.gson.GsonFactory;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.net.URL;

public class FacebookRealm extends AuthorizingRealm {
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return new SimpleAuthorizationInfo(); // TODO: Resolve roles by existing users in JDBC
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        AuthenticationInfo info = null;

        try {
            FacebookToken token = (FacebookToken) authenticationToken;

            String code = token.getCode();

            if (code != null && code.trim().length() > 0) {
                String APP_ID = FacebookSettings.getAppId();
                String REDIRECT_URL = FacebookSettings.getRedirectUrl();
                String APP_SECRET = FacebookSettings.getSecret();

                URL authUrl = new URL("https://graph.facebook.com/v2.3/oauth/access_token?client_id=" + APP_ID +
                        "&redirect_uri=" + REDIRECT_URL +
                        "&client_secret=" + APP_SECRET +
                        "&code=" + code);

                HttpTransport httpTransport = new ApacheHttpTransport();

                HttpRequestFactory factory = httpTransport.createRequestFactory();

                JsonObjectParser jsonObjectParser = new GsonFactory().createJsonObjectParser();

                HttpRequest getAuthRequest = factory.buildGetRequest(new GenericUrl(authUrl)).setParser(jsonObjectParser);

                HttpResponse response = getAuthRequest.execute();

                try {
                    if (response.isSuccessStatusCode()) {
                        FacebookOauthResponse facebookOauthResponse = response.parseAs(FacebookOauthResponse.class);

                        URL url = new URL("https://graph.facebook.com/me?access_token=" + facebookOauthResponse.AccessToken);

                        HttpRequest getUserInfoRequest = factory.buildGetRequest(new GenericUrl(url)).setParser(jsonObjectParser);
                        HttpResponse userInfoResponse = getUserInfoRequest.execute();

                        FacebookUserDetails fud = userInfoResponse.parseAs(FacebookUserDetails.class);

                        userInfoResponse.disconnect();

                        info = new FacebookAuthenticationInfo(fud, this.getName());
                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    response.disconnect();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return info;
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        if (token instanceof FacebookToken) {
            return true;
        }

        return false;
    }
}
