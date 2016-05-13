package api.handlers.form;

import api.business.entities.FormResult;
import api.business.entities.Role;
import api.business.entities.User;
import api.business.services.interfaces.IFormService;
import api.business.services.interfaces.IUserService;
import api.contracts.base.BaseResponse;
import api.contracts.base.ErrorCodes;
import api.contracts.base.ErrorDto;
import api.contracts.dto.FormDto;
import api.contracts.form.SubmitFormRequest;
import api.handlers.base.BaseHandler;
import org.apache.shiro.SecurityUtils;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Optional;


@Stateless
public class SubmitFormHandler extends BaseHandler<SubmitFormRequest, BaseResponse> {
    @Inject
    private IFormService formService;
    @Inject
    private IUserService userService;
    @PersistenceContext
    private EntityManager em;

    @Override
    public ArrayList<ErrorDto> validate(SubmitFormRequest request) {

        ArrayList<ErrorDto> errors = new ArrayList<>();
        if (!SecurityUtils.getSubject().isAuthenticated()) {
            errors.add(new ErrorDto("Not authenticated.", ErrorCodes.AUTHENTICATION_ERROR));
        }
        return errors;
    }

    @Override
    public BaseResponse handleBase(SubmitFormRequest request) {
        BaseResponse response = createResponse();

        User user = userService.getByUsername(SecurityUtils.getSubject().getPrincipal().toString());
        user = em.merge(user);

        for (FormDto dto : request.fields) {
            FormResult fr = new FormResult();
            fr.setUser(user);
            fr.setField(formService.getFieldByName(dto.name));
            fr.setValue(dto.value);
            em.merge(fr);
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
        return response;
    }

    @Override
    public BaseResponse createResponse() {
        return new BaseResponse();
    }
}
