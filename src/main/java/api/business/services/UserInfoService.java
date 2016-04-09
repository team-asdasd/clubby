package api.business.services;

import api.business.User;
import api.business.services.interfaces.IUserInfoService;
import api.configuration.EntityManagerContainer;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;

@RequestScoped
public class UserInfoService implements IUserInfoService {
    private EntityManager em = EntityManagerContainer.getEntityManager(); // TODO: Fix freaking injecting with @PersistenceContext... or not if we want to use env vars?

    public User get(int id) {
        return em.find(User.class, id);
    }
}
