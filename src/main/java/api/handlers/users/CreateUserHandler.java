package api.handlers.users;

import api.business.entities.Field;
import api.business.entities.Login;
import api.business.entities.User;
import api.business.services.interfaces.IFormService;
import api.business.services.interfaces.ILoginService;
import api.business.services.interfaces.IUserService;
import api.contracts.dto.SubmitFormDto;
import api.contracts.users.CreateUserRequest;
import api.contracts.base.BaseResponse;
import api.contracts.base.ErrorCodes;
import api.contracts.base.ErrorDto;
import api.handlers.base.BaseHandler;
import api.helpers.Validator;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;

@Stateless
public class CreateUserHandler extends BaseHandler<CreateUserRequest, BaseResponse> {
    @Inject
    private IUserService userService;
    @Inject
    private ILoginService loginService;
    @Inject
    private IFormService formService;
    @PersistenceContext
    private EntityManager em;

    @Override
    public ArrayList<ErrorDto> validate(CreateUserRequest request) {
        ArrayList<ErrorDto> errors = Validator.checkAllNotNull(request);

        if (request.name.length() < 1) {
            errors.add(new ErrorDto("name must be provided", ErrorCodes.VALIDATION_ERROR));
        }

        if (request.password.length() < 6) {
            errors.add(new ErrorDto("Password must bee at least 6 characters length", ErrorCodes.VALIDATION_ERROR));
        }

        if (!request.password.equals(request.passwordConfirm)) {
            errors.add(new ErrorDto("Passwords does not match", ErrorCodes.VALIDATION_ERROR));
        }

        if (request.email.length() < 5) {
            errors.add(new ErrorDto("email must be provided", ErrorCodes.VALIDATION_ERROR));
        }
        User user = userService.getByEmail(request.email);
        if (user != null) {
            if (SecurityUtils.getSubject().isAuthenticated()) {
                User current = userService.getByUsername(SecurityUtils.getSubject().getPrincipal().toString());
                if (!current.equals(user)) {
                    errors.add(new ErrorDto("email already taken", ErrorCodes.DUPLICATE_EMAIL));
                }
            } else {
                errors.add(new ErrorDto("email already taken", ErrorCodes.DUPLICATE_EMAIL));
            }
        }
        for (SubmitFormDto dto : request.fields) {
            Field field = formService.getFieldByName(dto.name);
            if (field == null)
                errors.add(new ErrorDto("Field " + dto.name + " not found", ErrorCodes.BAD_REQUEST));
            else if (field.getValidationRegex() != null && !field.getValidationRegex().isEmpty() && !dto.value.matches(field.getValidationRegex())) {
                errors.add(new ErrorDto("Field " + field.getDescription() + " does not match pattern", ErrorCodes.VALIDATION_ERROR));
            }
            if (field != null && field.getRequired() && dto.value.isEmpty()) {
                errors.add(new ErrorDto("Required field " + field.getDescription() + " is empty", ErrorCodes.VALIDATION_ERROR));
            }
        }

        return errors;
    }

    @Override
    public BaseResponse handleBase(CreateUserRequest request) {
        Login login = new Login();
        User user = new User();
        user.setName(request.name);
        user.setLogin(login);
        user.setPicture(request.picture);

        PasswordService passwordService = new DefaultPasswordService();
        String encryptedPassword = passwordService.encryptPassword(request.password);

        login.setUsername(request.email);
        login.setPassword(encryptedPassword);
        login.setUser(user);

        userService.createUser(user, login);
        em.refresh(login);
        formService.saveFormResults(request.fields, user);

        return createResponse();
    }

    @Override
    public BaseResponse createResponse() {
        return new BaseResponse();
    }
}
