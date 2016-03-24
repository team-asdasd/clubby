package security.shiro.facebook;

import com.google.api.client.util.Key;

public class FacebookOauthResponse  {
    @Key("access_token")
    public String AccessToken;

    @Key("token_type")
    public String TokenType;

    @Key("expires_in")
    public int ExpiresIn;
}
