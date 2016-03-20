package api.resources;

import com.google.api.client.http.*;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.gson.GsonFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import java.net.URL;

@Path("/login")
public class LoginController {
    @GET
    @Path("facebook")
    @Produces("application/json")
    public FacebookUserDetails facebook(@Context HttpServletRequest request) {
        FacebookUserDetails fud = null;
        try {
            String code = request.getParameter("code");

            if (code != null && code.trim().length() > 0) {
                String APP_ID = FacebookSettings.getAppId();
                String REDIRECT_URL = FacebookSettings.getRedirectUrl();
                String APP_SECRET = FacebookSettings.getSecret();

                URL authUrl = new URL("https://graph.facebook.com/v2.3/oauth/access_token?" + "client_id="
                        + APP_ID + "&redirect_uri=" + REDIRECT_URL + "&client_secret="
                        + APP_SECRET + "&code=" + code);

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
                        fud = userInfoResponse.parseAs(FacebookUserDetails.class);

                        userInfoResponse.disconnect();
                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    response.disconnect();
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        return fud;
    }
}

