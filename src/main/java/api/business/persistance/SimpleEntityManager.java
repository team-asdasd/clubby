package api.business.persistance;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Mindaugas on 30/04/2016.
 */
@ApplicationScoped
public class SimpleEntityManager implements ISimpleEntityManager {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public<T> T insert(T entity){
        try {
            em.persist(entity);
            em.flush();
        } catch (Exception e) {
            em.clear();
            throw e;
        }

        return entity;
    }

    @Transactional
    public<T> T update(T entity){
        T updated;
        try {
            updated = em.merge(entity);
            em.flush();
        } catch (Exception e) {
            em.clear();
            throw e;
        }
        return updated;
    }

    @Transactional
    public<T> void delete(T entity){
        try {
            em.remove(entity);
            em.flush();
        } catch (Exception e) {
            em.clear();
            throw e;
        }
    }

    @Transactional
    public<T> T getById(Class<T> entityClass, Object primaryKey){
        return em.find(entityClass, primaryKey);
    }

    @Transactional
    public<T> List<T> getAll(Class<T> entityClass){
        CriteriaQuery<T> criteria = em.getCriteriaBuilder().createQuery(entityClass);
        criteria.select(criteria.from(entityClass));
        List<T> listOfEntities = em.createQuery(criteria).getResultList();
        return listOfEntities;
    }

}
