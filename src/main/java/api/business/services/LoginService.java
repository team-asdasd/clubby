package api.business.services;

import api.business.entities.Login;
import api.business.services.interfaces.ILoginService;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;

@RequestScoped
public class LoginService implements ILoginService {
    @PersistenceContext(type= PersistenceContextType.EXTENDED)
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

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Login getByUserName(String username) {
        try {
            TypedQuery<Login> logins = em.createQuery("FROM logins WHERE username = :username", Login.class).setParameter("username", username);
            return logins.getSingleResult();
        }catch (Exception e) {
            return null;
        }

    }
}
