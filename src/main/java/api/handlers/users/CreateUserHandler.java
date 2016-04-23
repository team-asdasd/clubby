package api.handlers.users;

import api.business.entities.Login;
import api.business.entities.User;
import api.business.services.interfaces.ILoginService;
import api.business.services.interfaces.IUserService;
import api.contracts.requests.CreateUserRequest;
import api.contracts.requests.base.BaseRequest;
import api.contracts.responses.base.BaseResponse;
import api.contracts.responses.base.ErrorCodes;
import api.contracts.responses.base.ErrorDto;
import api.handlers.base.BaseHandler;
import api.helpers.Validator;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;

/**
 * Created by Mindaugas on 23/04/2016.
 */
@Stateless
public class CreateUserHandler extends BaseHandler<CreateUserRequest, BaseResponse> {

    @Inject
    private IUserService userService;

    @Inject
    private ILoginService loginService;

    @Override
    public ArrayList<ErrorDto> validate(CreateUserRequest request) {
        ArrayList errors = Validator.checkAllNotNull(request);

        if(request.password.length() < 6){
            errors.add(new ErrorDto("Passwords must bee atleast 6 characters length", ErrorCodes.VALIDATION_ERROR));
        }

        if(!request.password.equals(request.passwordConfirm)){
            errors.add(new ErrorDto("Passwords does not match", ErrorCodes.VALIDATION_ERROR));
        }

        if(userService.getByEmail(request.email) != null){
            errors.add(new ErrorDto("Email already taken", ErrorCodes.DUPLICATE_EMAIL));
        }

        if(loginService.getByUserName(request.userName) != null){
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

        login.setUsername(request.userName);
        login.setPassword(request.password);
        login.setUser(user);

        userService.createUser(user, login);

        return createResponse();
    }

    @Override
    public BaseResponse createResponse() {
        return new BaseResponse();
    }
}
