package api.business.services;

import api.business.entities.Login;
import api.business.services.interfaces.ILoginService;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
public class LoginService implements ILoginService {
    @PersistenceContext
    private EntityManager em;

    public void createLogin(Login login) {
        try {
            if (!em.contains(login)) {
                em.persist(login);
                em.flush();
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public Login getByUserName(String username) {
        try {
            TypedQuery<Login> logins = em.createQuery("SELECT l FROM Login l WHERE l.username = :username", Login.class).setParameter("username", username);
            return logins.getSingleResult();
        } catch (Exception e) {
            return null;
        }

    }
}
