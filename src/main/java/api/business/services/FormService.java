package api.business.services;

import api.business.entities.Field;
import api.business.entities.FormResult;
import api.business.entities.User;
import api.business.persistance.ISimpleEntityManager;
import api.business.services.interfaces.IFormService;
import api.business.services.interfaces.IUserService;
import org.apache.shiro.SecurityUtils;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class FormService implements IFormService {
    @PersistenceContext
    private EntityManager em;
    @Inject
    private ISimpleEntityManager simpleEntityManager;
    @Inject
    private IUserService userService;

    public List<Field> getForm() {
        return simpleEntityManager.getAll(Field.class);
    }

    void addField(Field field) {
        simpleEntityManager.insert(field);
    }

    public Field getFieldByName(String name) {
        try {
            return em.createQuery("SELECT F FROM Field F WHERE F.name = :name", Field.class).setParameter("name", name).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<FormResult> getMyFields() {
        User user = userService.getByUsername(SecurityUtils.getSubject().getPrincipal().toString());
        return em.createQuery("SELECT F FROM FormResult F WHERE F.user = :user", FormResult.class).setParameter("user", user).getResultList();
    }

}