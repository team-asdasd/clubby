package api.business.services;

import api.business.entities.Login;
import api.business.entities.User;
import api.business.services.interfaces.IUserService;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.context.RequestScoped;
import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.UUID;

@RequestScoped
public class UserService implements IUserService {

    @PersistenceContext
    private EntityManager em;

    public User get(int id) {
        return em.find(User.class, id);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public User getByEmail(String email) {
        try {
            TypedQuery<User> users = em.createQuery("FROM User WHERE email = :email", User.class).setParameter("email", email);
            return users.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    public void createUser(User user, Login login) {
        try {
            em.persist(user);
            em.persist(login);
            em.flush();
            Query q = em.createNativeQuery("INSERT INTO security.logins_roles (role_name, username) VALUES ('candidate', :username)");
            q.setParameter("username", login.getUsername());
            q.executeUpdate();
        } catch (Exception e) {
            em.clear();
            throw e;
        }
    }

    public void createFacebookUser(String name, String email) {
        User user = new User();
        Login login = new Login();
        user.setName(name);
        user.setEmail(email);
        user.setFacebookUser(true);
        user.setLogin(login);
        login.setUsername(email);
        login.setUser(user);
        login.setPassword(UUID.randomUUID().toString());

        createUser(user, login);
    }
}
