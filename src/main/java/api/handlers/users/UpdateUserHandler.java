package api.handlers.users;

import api.business.entities.Field;
import api.business.entities.User;
import api.business.services.interfaces.IFormService;
import api.business.services.interfaces.ILoginService;
import api.business.services.interfaces.IUserService;
import api.contracts.base.ErrorCodes;
import api.contracts.base.ErrorDto;
import api.contracts.dto.SubmitFormDto;
import api.contracts.users.UpdateUserRequest;
import api.contracts.users.UpdateUserResponse;
import api.handlers.base.BaseHandler;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;

@Stateless
public class UpdateUserHandler extends BaseHandler<UpdateUserRequest, UpdateUserResponse> {
    @Inject
    private IUserService userService;
    @Inject
    private ILoginService loginService;
    @Inject
    private IFormService formService;
    @PersistenceContext
    private EntityManager em;

    @Override
    public ArrayList<ErrorDto> validate(UpdateUserRequest request) {
        ArrayList<ErrorDto> errors = new ArrayList<>();

        if (!SecurityUtils.getSubject().isAuthenticated()) {
            errors.add(new ErrorDto("Not authenticated.", ErrorCodes.AUTHENTICATION_ERROR));
            return errors;
        }

        if (request == null) {
            errors.add(new ErrorDto("request is missing", ErrorCodes.VALIDATION_ERROR));
            return errors;
        }

        if (request.id <= 0) {
            errors.add(new ErrorDto("id is not valid", ErrorCodes.VALIDATION_ERROR));
            return errors;
        }

        User user = userService.get(request.id);
        if (user == null) {
            errors.add(new ErrorDto("User does not exist", ErrorCodes.NOT_FOUND));
        }

        if (request.name != null && request.name.length() < 1) {
            errors.add(new ErrorDto("name must be provided", ErrorCodes.VALIDATION_ERROR));
        }

        if (request.password != null && request.password.length() < 6) {
            errors.add(new ErrorDto("Password must bee at least 6 characters length", ErrorCodes.VALIDATION_ERROR));
        }

        if (request.password != null && request.passwordConfirm != null && !request.password.equals(request.passwordConfirm)) {
            errors.add(new ErrorDto("Passwords does not match", ErrorCodes.VALIDATION_ERROR));
        }

        if (request.email != null && request.email.length() < 5) {
            errors.add(new ErrorDto("Email must be provided", ErrorCodes.VALIDATION_ERROR));
        }

        User current = userService.get();
        if (!current.equals(user)) {
            errors.add(new ErrorDto("Email already taken", ErrorCodes.DUPLICATE_EMAIL));
        }
            errors.addAll(formService.validateFormFields(request.fields));

        return errors;
    }

    @Override
    public UpdateUserResponse handleBase(UpdateUserRequest request) {
        UpdateUserResponse response = createResponse();

        User user = userService.get(request.id);

        if (request.name != null) {
            user.setName(request.name);
        }

        if (request.email != null) {
            user.getLogin().setEmail(request.email);
        }

        if (request.picture != null) {
            user.setPicture(request.picture);
        }

        if (request.password != null) {
            PasswordService passwordService = new DefaultPasswordService();
            String encryptedPassword = passwordService.encryptPassword(request.password);

            user.getLogin().setPassword(encryptedPassword);
        }

        // TODO: Fields

        return response;
    }

    @Override
    public UpdateUserResponse createResponse() {
        return new UpdateUserResponse();
    }
}
