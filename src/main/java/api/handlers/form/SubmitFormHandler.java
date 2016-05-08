package api.handlers.form;

import api.business.entities.Form;
import api.business.entities.Role;
import api.business.entities.User;
import api.business.services.UserService;
import api.contracts.base.BaseResponse;
import api.contracts.base.ErrorCodes;
import api.contracts.base.ErrorDto;
import api.contracts.dto.FormState;
import api.contracts.requests.SubmitFormRequest;
import api.handlers.base.BaseHandler;
import org.apache.shiro.SecurityUtils;
import web.helpers.FormStateHelper;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Stateless
public class SubmitFormHandler extends BaseHandler<SubmitFormRequest, BaseResponse> {
    @Inject
    FormStateHelper st;
    @Inject
    UserService userService;
    @PersistenceContext
    EntityManager em;

    @Override
    public ArrayList<ErrorDto> validate(SubmitFormRequest request) {

        FormState state = st.getFormState();

        ArrayList<ErrorDto> errors = new ArrayList<>();
        if (!SecurityUtils.getSubject().isAuthenticated()) {
            errors.add(new ErrorDto("Not authenticated.", ErrorCodes.AUTHENTICATION_ERROR));
        }
        if (state.address && request.Address == null) {
            errors.add(new ErrorDto("Address is empty", ErrorCodes.VALIDATION_ERROR));
        }
        if (state.phoneNumber && request.PhoneNumber == null) {
            errors.add(new ErrorDto("Phone number is empty", ErrorCodes.VALIDATION_ERROR));
        }
        if (state.birthDate && request.Birthdate == null) {
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
        em.persist(form);
        user.setForm(form);

        Optional<Role> role = user.getLogin().getRoles().stream().filter(u -> u.getRoleName().equals("potentialCandidate")).findFirst();
        if (role.isPresent()) {
            Role r = new Role();
            r.setRoleName("candidate");
            r.setUsername(user.getLogin().getUsername());
            r = em.merge(r);
            user.getLogin().getRoles().add(r);
            em.flush();
            user.getLogin().getRoles().remove(role.get());
        }
        em.flush();
        return response;
    }

    @Override
    public BaseResponse createResponse() {
        return new BaseResponse();
    }
}