package api.business.services;

import api.business.entities.User;
import api.business.services.interfaces.IUserService;
import api.configuration.EntityManagerContainer;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

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
        em.getTransaction().begin();
        if (!em.contains(user)) {
            em.persist(user);
            em.flush();
        }
        em.getTransaction().commit();
    }
}
