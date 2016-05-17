package api.business.services;

import api.business.entities.Field;
import api.business.entities.FormResult;
import api.business.entities.Role;
import api.business.entities.User;
import api.business.persistance.ISimpleEntityManager;
import api.business.services.interfaces.IFormService;
import api.business.services.interfaces.IUserService;
import api.contracts.dto.FormInfoDto;
import api.contracts.dto.SubmitFormDto;
import org.apache.shiro.SecurityUtils;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public void saveFormResults(List<SubmitFormDto> formDtos, User user){
        for (SubmitFormDto dto : formDtos) {
            if (!dto.value.isEmpty()) {
                FormResult fr = getFormResult(dto.name, user.getId());
                if (fr != null)
                    fr = em.merge(fr);
                else {
                    fr = new FormResult();
                }
                fr.setUser(user);
                fr.setField(getFieldByName(dto.name));
                fr.setValue(dto.value);
                em.merge(fr);
            }
        }
        Optional<Role> role = user.getLogin().getRoles().stream().filter(u -> u.getRoleName().equals("potentialCandidate")).findFirst();
        if (role.isPresent()) {
            Role r = new Role();
            r.setRoleName("candidate");
            r.setUsername(user.getLogin().getUsername());
            em.persist(r);
            em.remove(role.get());
        }
        em.flush();
    }


}