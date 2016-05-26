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
import org.apache.shiro.subject.Subject;

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
        ArrayList<ErrorDto> errors = new ArrayList<>();

        Subject sub = SecurityUtils.getSubject();
        if (request.name == null || request.name.length() < 1) {
            errors.add(new ErrorDto("Name must be provided", ErrorCodes.VALIDATION_ERROR));
        }

        if (!sub.isAuthenticated() && request.password.length() < 6) {
            errors.add(new ErrorDto("Password must bee at least 6 characters length", ErrorCodes.VALIDATION_ERROR));
        }

        if (!sub.isAuthenticated() && !request.password.equals(request.passwordConfirm)) {
            errors.add(new ErrorDto("Passwords does not match", ErrorCodes.VALIDATION_ERROR));
        }

        if (request.email == null || request.email.length() < 5) {
            errors.add(new ErrorDto("Email must be provided", ErrorCodes.VALIDATION_ERROR));
        }
        User user = userService.getByUsername(request.email);
        if (user != null) {
            if (SecurityUtils.getSubject().isAuthenticated()) {
                User current = userService.get();
                if (!current.equals(user)) {
                    errors.add(new ErrorDto("Email already taken", ErrorCodes.DUPLICATE_EMAIL));
                }
            } else {
                errors.add(new ErrorDto("Email already taken", ErrorCodes.DUPLICATE_EMAIL));
            }
        }
        errors.addAll(formService.validateFormFields(request.fields));
        return errors;
    }

    @Override
    public BaseResponse handleBase(CreateUserRequest request) {
        Subject sub = SecurityUtils.getSubject();
        Login login;
        User user;
        if (sub.isAuthenticated() && !sub.hasRole("administrator")) {
            user = userService.get();
            login = user.getLogin();
            user.setName(request.name);
            user.setPicture(request.picture);
            login.setEmail(request.email);
        } else {
            login = new Login();
            user = new User();
            user.setName(request.name);
            user.setLogin(login);
            user.setPicture(request.picture);

            PasswordService passwordService = new DefaultPasswordService();
            String encryptedPassword = passwordService.encryptPassword(request.password);

            login.setEmail(request.email);
            login.setPassword(encryptedPassword);

            userService.createUser(user, login);
            em.refresh(login);
        }
        formService.saveFormResults(request.fields, user);
        return createResponse();
    }

    @Override
    public BaseResponse createResponse() {
        return new BaseResponse();
    }
}
