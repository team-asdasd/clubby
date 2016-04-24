package api.business.services;

import api.business.entities.Cottage;
import api.business.entities.User;
import api.business.services.interfaces.ICottageService;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import java.util.List;

@RequestScoped
public class CottageService implements ICottageService {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Cottage> getAllCottages() {
        TypedQuery<Cottage> cottages = em.createQuery("FROM Cottage", Cottage.class);

        return cottages.getResultList();
    }

    @Override
    public void createCottage(Cottage cottage) {
        try {
            em.persist(cottage);
            em.flush();
        } catch (Exception e) {
            em.clear();
            throw e;
        }
    }
}
