package clients.facebook.responses;

import com.google.api.client.util.Key;

public class FacebookProfilePictureSource {
    @Key("url")
    public String Url;

    @Key("is_silhouette")
    public boolean IsSilhouette;
}
