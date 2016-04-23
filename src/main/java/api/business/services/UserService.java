package api.business.services;

import api.business.entities.Login;
import api.business.entities.User;
import api.business.services.interfaces.IUserService;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.UUID;

@RequestScoped
public class UserService implements IUserService {

    @PersistenceContext(type= PersistenceContextType.EXTENDED)
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
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void createUser(User user) {
        try {
            if (!em.contains(user)) {
                em.persist(user);
                em.flush();
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    @Transactional
    public void createFacebookUser(String name, String email) {
        try {
            User user = new User();
            Login login = new Login();
            user.setName(name);
            user.setEmail(email);
            user.setFacebookUser(true);
            user.setLogin(login);
            login.setUsername(email);
            login.setUser(user);
            login.setPassword(UUID.randomUUID().toString());
            em.persist(user);
            em.persist(login);
            em.flush();
        } catch (Exception e) {
            //TODO log
            em.clear();
        }
    }
}
