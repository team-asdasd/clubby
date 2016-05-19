package api.business.persistance;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by Mindaugas on 30/04/2016.
 */
public interface ISimpleEntityManager {
    EntityManager getEntityManager();

    <T> T insert(T entity);

    <T> T update(T entity);

    <T> void delete(T entity);

    <T> T getById(Class<T> entityClass, Object primaryKey);

    <T> List<T> getAll(Class<T> entityClass);
}
