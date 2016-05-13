package api.handlers.form;

import api.business.entities.Form;
import api.business.entities.Role;
import api.business.entities.User;
import api.business.services.UserService;
import api.business.services.interfaces.IUserService;
import api.contracts.base.BaseResponse;
import api.contracts.base.ErrorCodes;
import api.contracts.base.ErrorDto;
import api.contracts.dto.FormState;
import api.contracts.form.SubmitFormRequest;
import api.handlers.base.BaseHandler;
import org.apache.shiro.SecurityUtils;
import web.helpers.FormStateHelper;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Optional;


@Stateless
public class SubmitFormHandler extends BaseHandler<SubmitFormRequest, BaseResponse> {
    @Inject
    FormStateHelper st;
    @Inject
    IUserService userService;
    @PersistenceContext
    EntityManager em;

    @Override
    public ArrayList<ErrorDto> validate(SubmitFormRequest request) {

        FormState state = st.getFormState();

        ArrayList<ErrorDto> errors = new ArrayList<>();
        if (!SecurityUtils.getSubject().isAuthenticated()) {
            errors.add(new ErrorDto("Not authenticated.", ErrorCodes.AUTHENTICATION_ERROR));
        }
        if (state.Address && request.Address == null) {
            errors.add(new ErrorDto("ShowAddress is empty", ErrorCodes.VALIDATION_ERROR));
        }
        if (state.PhoneNumber && request.PhoneNumber == null) {
            errors.add(new ErrorDto("Phone number is empty", ErrorCodes.VALIDATION_ERROR));
        }
        if (state.BirthDate && request.Birthdate == null) {
            errors.add(new ErrorDto("Birthdate is empty", ErrorCodes.VALIDATION_ERROR));
        }


        return errors;
    }

    @Override
    public BaseResponse handleBase(SubmitFormRequest request) {
        BaseResponse response = createResponse();

        User user = userService.getByUsername(SecurityUtils.getSubject().getPrincipal().toString());
        user = em.merge(user);

        Form form = new Form();
        form.setAbout(request.About);
        form.setAddress(request.Address);
        form.setBirthdate(Date.valueOf(request.Birthdate));
        form.setPhoneNumber(request.PhoneNumber);
        form.setPhotoUrl(request.Photo);
        form = em.merge(form);
        user.setForm(form);

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
