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
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Stateless
public class GetAllUsersHandler extends BaseHandler<GetAllUsersRequest, GetAllUsersResponse> {
    @PersistenceContext
    private EntityManager em;
    @Inject
    private UserMapper mapper;

    @Override
    public ArrayList<ErrorDto> validate(GetAllUsersRequest request) {
        ArrayList<ErrorDto> authErrors = new Validator().isAuthenticated().getErrors();

        if (!authErrors.isEmpty()) return authErrors;

        return new Validator().isMember().allFieldsSet(request).getErrors();
    }

    @Override
    public GetAllUsersResponse handleBase(GetAllUsersRequest request) {
        Configuration default_user_picture_url = em.find(Configuration.class, "default_user_picture_url");
        String defaultPic = default_user_picture_url != null ? default_user_picture_url.getValue() : "";

        GetAllUsersResponse response = createResponse();

        response.users = em.createQuery("SELECT U FROM User U WHERE U.login.disabled = false",User.class).getResultList().stream().map(u -> mapper.map(u, defaultPic)).collect(Collectors.toList());

        return response;
    }

    @Override
    public GetAllUsersResponse createResponse() {
        return new GetAllUsersResponse();
    }
}
