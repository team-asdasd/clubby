package api.handlers.users;

import api.business.entities.Configuration;
import api.business.entities.User;
import api.business.persistance.ISimpleEntityManager;
import api.business.services.interfaces.IFormService;
import api.business.services.interfaces.IUserService;
import api.contracts.base.ErrorCodes;
import api.contracts.base.ErrorDto;
import api.contracts.users.GetUserByIdRequest;
import api.contracts.users.GetUserInfoResponse;
import api.handlers.base.BaseHandler;
import api.helpers.Validator;
import api.helpers.mappers.UserMapper;
import clients.facebook.interfaces.IFacebookClient;
import clients.facebook.responses.FacebookUserDetails;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;

@Stateless
public class GetUserByIdHandler extends BaseHandler<GetUserByIdRequest, GetUserInfoResponse> {
    @Inject
    private ISimpleEntityManager em;
    @Inject
    private IUserService userInfoService;
    @Inject
    private IFacebookClient facebookClient;
    @Inject
    private IFormService formService;
    @Inject
    private UserMapper mapper;

    @Override
    public ArrayList<ErrorDto> validate(GetUserByIdRequest request) {
        return Validator.checkAllNotNullAndIsAuthenticated(request);
    }

    @Override
    public GetUserInfoResponse handleBase(GetUserByIdRequest request) {
        Configuration default_user_picture_url = em.getById(Configuration.class, "default_user_picture_url");
        String defaultPic = default_user_picture_url != null ? default_user_picture_url.getValue() : "";
        
        GetUserInfoResponse response = createResponse();
        User user = userInfoService.get(request.id);

        if (user == null) {
            logger.warn(String.format("User %s not found", request.id));
            return handleException("User not found", ErrorCodes.NOT_FOUND);
        }

        response.id = user.getId();
        response.name = user.getName();
        response.email = user.getLogin().getEmail();
        response.picture = mapper.getPicture(user, defaultPic);
        response.fields = formService.getFormByUserId(request.id);

        return response;
    }

    @Override
    public GetUserInfoResponse createResponse() {
        return new GetUserInfoResponse();
    }
}
