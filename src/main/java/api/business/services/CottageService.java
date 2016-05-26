package api.business.services;

import api.business.entities.Cottage;
import api.business.entities.Service;
import api.business.services.interfaces.ICottageService;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class CottageService implements ICottageService {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Cottage> getByFilters(String title, int beds) {
        String titleFilter = title != null ? '%' + title + '%' : "";

        TypedQuery<Cottage> cottages = em.createQuery("SELECT C FROM Cottage C WHERE (:title = '' OR lower(C.title) LIKE lower(:title)) AND (:beds = 0 OR C.bedcount = :beds) ORDER BY C.id", Cottage.class)
                .setParameter("title", titleFilter)
                .setParameter("beds", beds);

        return cottages.getResultList();
    }

    @Override
    public void save(Cottage cottage) {
        try {
            em.persist(cottage);
            em.flush();
        } catch (Exception e) {
            em.clear();
            throw e;
        }
    }

    @Override
    public Cottage get(int id) {
        return em.find(Cottage.class, id);
    }

    @Override
    public void delete(int id) {
        Cottage cottage = get(id);
        em.remove(cottage);
    }

    @Override
    public List<Service> getCottageServices(int id) {
        return em.createQuery("SELECT S FROM Service S WHERE S.cottage.id = :cottageId", Service.class).setParameter("cottageId", id).getResultList();
    }
}
