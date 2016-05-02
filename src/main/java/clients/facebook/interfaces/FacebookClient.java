package clients.facebook.interfaces;

import clients.facebook.FacebookSettings;
import clients.facebook.responses.FacebookUserDetails;
import com.google.api.client.http.*;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.gson.GsonFactory;
import org.apache.shiro.SecurityUtils;

import javax.enterprise.context.RequestScoped;
import java.io.IOException;
import java.net.URL;

@RequestScoped
public class FacebookClient implements IFacebookClient {
    @Override
    public FacebookUserDetails getMyDetails() throws IOException {
        HttpResponse userInfoResponse = null;
        FacebookUserDetails fud = null;
        try {
            HttpTransport httpTransport = new ApacheHttpTransport();

            HttpRequestFactory factory = httpTransport.createRequestFactory();

            JsonObjectParser jsonObjectParser = new GsonFactory().createJsonObjectParser();

            String accessToken = GetAccessToken();

            URL url = new URL("https://graph.facebook.com/v2.5/me?fields=name,email,picture&access_token=" + accessToken);

            HttpRequest getUserInfoRequest = factory.buildGetRequest(new GenericUrl(url)).setParser(jsonObjectParser);
            userInfoResponse = getUserInfoRequest.execute();

            fud = userInfoResponse.parseAs(FacebookUserDetails.class);
            userInfoResponse.disconnect();
        } catch (Exception ignored) {

        } finally {
            userInfoResponse.disconnect();
        }

        return fud;
    }

    @Override
    public FacebookUserDetails getUserDetailsById(String id) throws IOException {
        HttpResponse userInfoResponse = null;
        FacebookUserDetails fud = null;
        try {
            HttpTransport httpTransport = new ApacheHttpTransport();

            HttpRequestFactory factory = httpTransport.createRequestFactory();

            JsonObjectParser jsonObjectParser = new GsonFactory().createJsonObjectParser();

            String accessToken = FacebookSettings.getAppAccessToken();
            String urlString = String.format("https://graph.facebook.com/v2.5/%s?fields=name,email,picture&access_token=%s", id, accessToken);
            URL url = new URL(urlString);

            HttpRequest getUserInfoRequest = factory.buildGetRequest(new GenericUrl(url)).setParser(jsonObjectParser);
            userInfoResponse = getUserInfoRequest.execute();

            fud = userInfoResponse.parseAs(FacebookUserDetails.class);
        } catch (Exception ignored) {

        } finally {
            userInfoResponse.disconnect();
        }

        return fud;
    }

    private String GetAccessToken() {
        return SecurityUtils.getSubject().getSession().getAttribute(FacebookSettings.getAccessTokenKey()).toString();
    }
}
