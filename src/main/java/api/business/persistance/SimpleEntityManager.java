package api.business.persistance;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

@Stateless
public class SimpleEntityManager implements ISimpleEntityManager {

    @PersistenceContext
    private EntityManager em;

    private final Logger logger = LogManager.getLogger(getClass().getName());

    public <T> T insert(T entity) {
        try {
            em.persist(entity);
        } catch (Exception e) {
            em.clear();
            logException(e);
            throw e;
        }

        return entity;
    }

    public <T> T update(T entity) {
        T updated;
        try {
            updated = em.merge(entity);
            em.flush();
        } catch (Exception e) {
            em.clear();
            logException(e);
            throw e;
        }
        return updated;
    }

    public <T> void delete(T entity) {
        try {
            em.remove(entity);
            em.flush();
        } catch (Exception e) {
            em.clear();
            logException(e);
            throw e;
        }
    }

    public <T> T getById(Class<T> entityClass, Object primaryKey) {
        T entity = null;
        try {
            entity = em.find(entityClass, primaryKey);
        } catch (Exception ignored) {
        }

        return entity;
    }

    public <T> List<T> getAll(Class<T> entityClass) {
        CriteriaQuery<T> criteria = em.getCriteriaBuilder().createQuery(entityClass);
        criteria.select(criteria.from(entityClass));

        return em.createQuery(criteria).getResultList();
    }

    private void logException(Exception e) {
        logger.warn(ExceptionUtils.getRootCause(e));
    }
}
