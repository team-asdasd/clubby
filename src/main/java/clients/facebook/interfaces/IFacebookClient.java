package clients.facebook.interfaces;

import clients.facebook.responses.FacebookUserDetails;

import java.io.IOException;

public interface IFacebookClient {
    FacebookUserDetails getUserDetails() throws IOException;
    FacebookUserDetails getUserDetailsById(String id) throws IOException;
}