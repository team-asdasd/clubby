package api.business.services;

import api.business.entities.Login;
import api.business.entities.Role;
import api.business.entities.User;
import api.business.services.interfaces.IUserService;
import clients.facebook.responses.FacebookUserDetails;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.subject.Subject;

import javax.ejb.Stateless;
import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Stateless
public class UserService implements IUserService {
    @PersistenceContext
    private EntityManager em;

    public User get(int id) {
        try {
            return em.createQuery("SELECT U FROM User U WHERE U.id = :id AND U.login.disabled = false", User.class)
                    .setParameter("id", id).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public User get() {
        Subject sub = SecurityUtils.getSubject();
        if (sub.isAuthenticated())
            return get(Integer.parseInt(sub.getPrincipal().toString()));
        return null;
    }

    public User getByEmail(String email) {
        try {
            TypedQuery<User> users = em.createQuery("SELECT U FROM User U WHERE U.login.email = :email AND U.login.disabled = false", User.class)
                    .setParameter("email", email);
            return users.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public void createUser(User user, Login login) {
        try {
            em.persist(user);
            em.persist(login);
            Role lr = new Role();
            lr.setRoleName("potentialCandidate");
            lr.setUsername(user.getLogin().getEmail());
            em.persist(lr);
            em.flush();
        } catch (Exception e) {
            em.clear();
            throw e;
        }
    }

    public void createFacebookUser(FacebookUserDetails details) {
        User user = new User();
        Login login = new Login();

        user.setName(details.Name);
        login.setFacebookId(details.Id);
        user.setPicture(details.Picture.getUrl());
        user.setLogin(login);

        login.setEmail(details.Email);

        PasswordService passwordService = new DefaultPasswordService();
        String encryptedPassword = passwordService.encryptPassword(UUID.randomUUID().toString());

        login.setPassword(encryptedPassword);

        createUser(user, login);
    }

    @Override
    public User getByFacebookId(String id) {
        try {
            TypedQuery<User> users = em.createQuery("SELECT U FROM User U WHERE U.login.facebookId = :id", User.class).setParameter("id", id);
            return users.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
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
        List<User> userList = em.createQuery("SELECT u FROM User u WHERE u.login.email = :username AND u.login.disabled = false ", User.class)
                .setParameter("username", username)
                .getResultList();
        if (userList.size() == 0)
            return null;
        return userList.get(0);
    }

    @Override
    public void disableUser(int id) {
        get(id).getLogin().setDisabled(true);
    }

    public void disableUser() {
        get().getLogin().setDisabled(true);
    }
}
