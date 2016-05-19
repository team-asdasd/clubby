package api.handlers.users;

import api.business.entities.Login;
import api.business.entities.User;
import api.business.services.interfaces.ILoginService;
import api.business.services.interfaces.IUserService;
import api.contracts.users.CreateUserRequest;
import api.contracts.base.BaseResponse;
import api.contracts.base.ErrorCodes;
import api.contracts.base.ErrorDto;
import api.handlers.base.BaseHandler;
import api.helpers.Validator;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;

@Stateless
public class CreateUserHandler extends BaseHandler<CreateUserRequest, BaseResponse> {
    @Inject
    private IUserService userService;

    @Inject
    private ILoginService loginService;

    @Override
    public ArrayList<ErrorDto> validate(CreateUserRequest request) {
        ArrayList<ErrorDto> errors = Validator.checkAllNotNull(request);

        if (request.firstName.length() < 1) {
            errors.add(new ErrorDto("First name must be provided", ErrorCodes.VALIDATION_ERROR));
        }

        if (request.lastName.length() < 1) {
            errors.add(new ErrorDto("Last name must be provided", ErrorCodes.VALIDATION_ERROR));
        }

        if (request.userName.length() < 6) {
            errors.add(new ErrorDto("Username must be at least 6 characters length", ErrorCodes.VALIDATION_ERROR));
        }

        if (request.password.length() < 6) {
            errors.add(new ErrorDto("Password must bee at least 6 characters length", ErrorCodes.VALIDATION_ERROR));
        }

        if (!request.password.equals(request.passwordConfirm)) {
            errors.add(new ErrorDto("Passwords does not match", ErrorCodes.VALIDATION_ERROR));
        }

        if (request.email.length() < 5) {
            errors.add(new ErrorDto("Email must be provided", ErrorCodes.VALIDATION_ERROR));
        }

        if (userService.getByEmail(request.email) != null) {
            errors.add(new ErrorDto("Email already taken", ErrorCodes.DUPLICATE_EMAIL));
        }

        if (loginService.getByUserName(request.userName) != null) {
            errors.add(new ErrorDto("User name already taken", ErrorCodes.DUPLICATE_USERNAME));
        }

        return errors;
    }

    @Override
    public BaseResponse handleBase(CreateUserRequest request) {
        Login login = new Login();
        User user = new User();

        user.setEmail(request.email);
        user.setName(request.firstName + " " + request.lastName);
        user.setLogin(login);

        PasswordService passwordService = new DefaultPasswordService();
        String encryptedPassword = passwordService.encryptPassword(request.password);

        login.setUsername(request.userName);
        login.setPassword(encryptedPassword);
        login.setUser(user);

        userService.createUser(user, login);

        return createResponse();
    }

    @Override
    public BaseResponse createResponse() {
        return new BaseResponse();
    }
}
