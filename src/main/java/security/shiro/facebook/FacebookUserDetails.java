package security.shiro.facebook;

import com.google.api.client.util.Key;

public class FacebookUserDetails {
    @Key("id")
    public String Id;

    @Key("name")
    public String Name;
}