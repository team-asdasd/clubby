package api.business.services.interfaces;

import api.business.entities.Login;
import api.business.entities.User;
import clients.facebook.responses.FacebookUserDetails;

public interface IUserService {
    User get(int id);

    User getByEmail(String email);

    void createUser(User user, Login login);

    void createFacebookUser(FacebookUserDetails details);

    User getByFacebookId(String id);

    void save(User user);
}
