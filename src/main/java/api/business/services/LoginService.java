package api.business.services;

import api.business.entities.Login;
import api.business.services.interfaces.ILoginService;
import api.configuration.EntityManagerContainer;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;

@RequestScoped
public class LoginService implements ILoginService {
    private EntityManager em = EntityManagerContainer.getEntityManager(); // TODO: Fix freaking injecting with @PersistenceContext... or not if we want to use env vars?

    public void createLogin(Login login) {
        try {
            em.getTransaction().begin();
            if (!em.contains(login)) {
                em.persist(login);
                em.flush();
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
    }
}
