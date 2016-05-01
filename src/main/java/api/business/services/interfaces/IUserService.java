package api.business.services.interfaces;

import api.business.entities.Login;
import api.business.entities.User;

public interface IUserService {
    User get(int id);

    User getByEmail(String email);

    void createUser(User user, Login login);

    void createFacebookUser(String name, String email);
}
