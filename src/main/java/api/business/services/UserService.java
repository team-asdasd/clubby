package api.business.services;

import api.business.entities.Login;
import api.business.entities.Role;
import api.business.entities.User;
import api.business.services.interfaces.IUserService;
import clients.facebook.responses.FacebookUserDetails;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.context.RequestScoped;
import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.List;
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
            Role lr = new Role();
            lr.setRoleName("potentialCandidate");
            lr.setUsername(login.getUsername());
            em.persist(lr);
            em.flush();
        } catch (Exception e) {
            em.clear();
            throw e;
        }
    }

    @Transactional
    public void createFacebookUser(FacebookUserDetails details) {
        User user = new User();
        Login login = new Login();
        user.setName(details.Name);
        user.setEmail(details.Email);
        user.setFacebookId(details.Id);
        user.setLogin(login);
        login.setUsername(details.Email);
        login.setUser(user);
        login.setPassword(UUID.randomUUID().toString());

        createUser(user, login);
    }

    @Override
    public User getByFacebookId(String id) {
        try {
            TypedQuery<User> users = em.createQuery("FROM User WHERE facebookId = :id", User.class).setParameter("id", id);
            return users.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    @Transactional
    public void save(User user) {
        try {
            em.merge(user);
            em.flush();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public User getByUsername(String username) {
        List<User> userList = em.createQuery("SELECT u FROM User u WHERE u.login.username = :username", User.class)
                .setParameter("username", username)
                .getResultList();
        if(userList.size() == 0)
            return null;
        return userList.get(0);
    }
}
