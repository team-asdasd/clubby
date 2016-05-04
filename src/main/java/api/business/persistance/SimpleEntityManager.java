package api.business.persistance;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private final Logger logger = LogManager.getLogger(getClass().getName());

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
        T entity = null;
        try{
            entity = em.find(entityClass, primaryKey);
        }catch (Exception e){
            logger.error(e);
        }

        return entity;
    }

    @Transactional
    public<T> List<T> getAll(Class<T> entityClass){
        CriteriaQuery<T> criteria = em.getCriteriaBuilder().createQuery(entityClass);
        criteria.select(criteria.from(entityClass));

        return em.createQuery(criteria).getResultList();
    }

}
