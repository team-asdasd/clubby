package clients.facebook.responses;

import clients.facebook.responses.FacebookPicture;
import com.google.api.client.util.Key;

public class FacebookUserDetails {
    @Key("id")
    public String Id;

    @Key("name")
    public String Name;

    @Key("email")
    public String Email;

    @Key("picture")
    public FacebookPicture Picture;
}
