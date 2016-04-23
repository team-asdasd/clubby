package api.business.services;

import api.business.entities.Login;
import api.business.entities.User;
import api.business.services.interfaces.IUserService;
import api.configuration.EntityManagerContainer;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.crypto.hash.Sha256Hash;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.UUID;

@RequestScoped
public class UserService implements IUserService {
    private EntityManager em = EntityManagerContainer.getEntityManager(); // TODO: Fix freaking injecting with @PersistenceContext... or not if we want to use env vars?

    public User get(int id) {
        return em.find(User.class, id);
    }

    public User getByEmail(String email) {
        try {
            TypedQuery<User> users = em.createQuery("FROM User WHERE email = :email", User.class).setParameter("email", email);
            return users.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public void createUser(User user, Login login) {
        //todo use encrypted password
        /*PasswordService passwordService = new DefaultPasswordService();
        String encrypted = passwordService.encryptPassword(login.getPassword());

        login.setPassword(encrypted);
*/
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();

            em.persist(user);
            em.persist(login);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    @Override
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
