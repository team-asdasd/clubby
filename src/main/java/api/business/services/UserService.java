package api.business.services;

import api.business.entities.Login;
import api.business.entities.User;
import api.business.services.interfaces.IUserService;
import api.configuration.EntityManagerContainer;

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

    public void createUser(User user) {
        try {
            em.getTransaction().begin();
            if (!em.contains(user)) {
                em.persist(user);
                em.flush();
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public void createFacebookUser(String name, String email) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();

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

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
    }
}
