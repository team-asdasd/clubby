package api.handlers.users;

import api.business.entities.Configuration;
import api.business.entities.Field;
import api.business.entities.Role;
import api.business.entities.User;
import api.business.persistance.ISimpleEntityManager;
import api.business.services.interfaces.IFormService;
import api.business.services.interfaces.ILoginService;
import api.business.services.interfaces.IUserService;
import api.contracts.base.BaseRequest;
import api.contracts.base.ErrorCodes;
import api.contracts.base.ErrorDto;
import api.contracts.dto.FormInfoDto;
import api.contracts.users.GetUserInfoResponse;
import api.handlers.base.BaseHandler;
import api.helpers.validator.Validator;
import api.helpers.mappers.UserMapper;
import clients.facebook.interfaces.IFacebookClient;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Stateless
public class GetCurrentUserHandler extends BaseHandler<BaseRequest, GetUserInfoResponse> {
    @Inject
    private ISimpleEntityManager em;
    @Inject
    private IUserService userService;
    @Inject
    private ILoginService loginService;
    @Inject
    private IFacebookClient facebookClient;
    @Inject
    private IFormService formService;
    @Inject
    private UserMapper mapper;

    @Override
    public ArrayList<ErrorDto> validate(BaseRequest request) {
        ArrayList<ErrorDto> authErrors = new Validator().isAuthenticated().getErrors();

        if (!authErrors.isEmpty()) return authErrors;

        ArrayList<ErrorDto> errors = new Validator().allFieldsSet(request).getErrors();

        User user = userService.get();

        if (user == null) {
            errors.add(new ErrorDto("user not found", ErrorCodes.NOT_FOUND));
        }

        return errors;
    }

    @Override
    public GetUserInfoResponse handleBase(BaseRequest request) {
        Configuration default_user_picture_url = em.getById(Configuration.class, "default_user_picture_url");
        String defaultPic = default_user_picture_url != null ? default_user_picture_url.getValue() : "";

        User user = userService.get();

        GetUserInfoResponse response = createResponse();
        response.id = user.getId();
        response.name = user.getName();
        response.email = user.getLogin().getEmail();
        response.picture = mapper.getPicture(user, defaultPic);
        response.fields = user.getFormResults().stream().map(FormInfoDto::new).collect(Collectors.toList());
        response.roles = user.getLogin().getRoles().stream().map(Role::getRoleName).collect(Collectors.toList());
        response.fields.addAll(formService.getVisibleFields().stream().filter(f -> !haveResult(f, response)).map(FormInfoDto::new).collect(Collectors.toList()));
        response.group = user.activeGroup();

        return response;
    }

    private boolean haveResult(Field f,GetUserInfoResponse response ) {
        return response.fields.stream().map(r -> r.name).collect(Collectors.toList()).contains(f.getName());
    }

    @Override
    public GetUserInfoResponse createResponse() {
        return new GetUserInfoResponse();
    }
}
