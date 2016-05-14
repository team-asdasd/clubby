package api.business.services;

import api.business.entities.Field;
import api.business.entities.FormResult;
import api.business.entities.User;
import api.business.persistance.ISimpleEntityManager;
import api.business.services.interfaces.IFormService;
import api.business.services.interfaces.IUserService;
import api.contracts.dto.FormInfoDto;
import org.apache.shiro.SecurityUtils;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
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

    @Override
    public List<Field> getVisibleFields() {
        return em.createQuery("SELECT F FROM Field F WHERE F.visible = true", Field.class).getResultList();
    }

    @Override
    public List<FormInfoDto> getFormByUserId(int id) {
        List<FormInfoDto> list = new ArrayList<>();
        List<FormResult> results = em.createQuery("SELECT R FROM FormResult R WHERE R.user.id = :id", FormResult.class).setParameter("id", id).getResultList();
        for (FormResult res : results) {
            FormInfoDto info = new FormInfoDto();
            info.value = res.getValue();
            info.description = res.getField().getDescription();
            info.type = res.getField().getType();
            list.add(info);
        }
        return list;
    }

    @Override
    public FormResult getFormResult(String fieldName, int userId) {
        try {
            return em.createQuery("SELECT R FROM FormResult R WHERE R.user.id = :userId AND R.field.name = :fieldName", FormResult.class)
                    .setParameter("userId", userId)
                    .setParameter("fieldName", fieldName)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

}