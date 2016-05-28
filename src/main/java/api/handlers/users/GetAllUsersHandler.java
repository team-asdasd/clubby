package api.handlers.users;

import api.business.entities.Configuration;
import api.business.entities.User;
import api.business.persistance.ISimpleEntityManager;
import api.contracts.base.ErrorDto;
import api.contracts.users.GetAllUsersRequest;
import api.contracts.users.GetAllUsersResponse;
import api.handlers.base.BaseHandler;
import api.helpers.validator.Validator;
import api.helpers.mappers.UserMapper;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Stateless
public class GetAllUsersHandler extends BaseHandler<GetAllUsersRequest, GetAllUsersResponse> {
    @Inject
    private ISimpleEntityManager em;
    @Inject
    private UserMapper mapper;

    @Override
    public ArrayList<ErrorDto> validate(GetAllUsersRequest request) {
        ArrayList<ErrorDto> authErrors = new Validator().isAuthenticated().getErrors();

        if (!authErrors.isEmpty()) return authErrors;

        return new Validator().isAdministrator().allFieldsSet(request).getErrors();
    }

    @Override
    public GetAllUsersResponse handleBase(GetAllUsersRequest request) {
        Configuration default_user_picture_url = em.getById(Configuration.class, "default_user_picture_url");
        String defaultPic = default_user_picture_url != null ? default_user_picture_url.getValue() : "";

        GetAllUsersResponse response = createResponse();

        response.users = em.getAll(User.class).stream().map(u -> mapper.map(u, defaultPic)).collect(Collectors.toList());

        return response;
    }

    @Override
    public GetAllUsersResponse createResponse() {
        return new GetAllUsersResponse();
    }
}
