package clients.facebook.responses;

import com.google.api.client.util.Key;

public class FacebookPicture {
    @Key("data")
    private FacebookProfilePictureSource data;

    public String getUrl() {
        return data.Url;
    }

    public boolean isSilhouette() {
        return data.IsSilhouette;
    }
}

