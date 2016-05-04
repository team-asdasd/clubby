package api.contracts.users;

import api.business.entities.User;
import api.business.persistance.ISimpleEntityManager;
import api.contracts.base.ErrorDto;
import api.contracts.dto.UserDto;
import api.handlers.base.BaseHandler;
import api.helpers.Validator;
import org.apache.shiro.SecurityUtils;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class GetAllUsersHandler extends BaseHandler<GetAllUsersRequest, GetAllUsersResponse> {
    @Inject
    private ISimpleEntityManager entityManager;

    @Override
    public ArrayList<ErrorDto> validate(GetAllUsersRequest request) {
        return Validator.checkAllNotNullAndIsAuthenticated(request);
    }

    @Override
    public GetAllUsersResponse handleBase(GetAllUsersRequest request) {
        GetAllUsersResponse response = createResponse();

        response.Users = entityManager.getAll(User.class).stream().map(UserDto::new).collect(Collectors.toList());

        if (!SecurityUtils.getSubject().hasRole("administrator")) {
            response.Users.forEach(u -> u.Email = null);
        }

        return response;
    }

    @Override
    public GetAllUsersResponse createResponse() {
        return new GetAllUsersResponse();
    }
}
