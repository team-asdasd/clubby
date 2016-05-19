package api.handlers.users;

import api.contracts.users.HasRoleRequest;
import api.contracts.users.HasRoleResponse;
import api.contracts.base.ErrorCodes;
import api.contracts.base.ErrorDto;
import api.handlers.base.BaseHandler;
import api.helpers.Validator;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.ejb.Stateless;
import java.util.ArrayList;

@Stateless
public class HasRoleHandler extends BaseHandler<HasRoleRequest, HasRoleResponse> {
    @Override
    public ArrayList<ErrorDto> validate(HasRoleRequest request) {

        ArrayList<ErrorDto> errors = Validator.checkAllNotNullAndIsAuthenticated(request);

        if (request.RoleName.isEmpty()) {
            errors.add(new ErrorDto("RoleName empty.", ErrorCodes.VALIDATION_ERROR));
            return errors;
        }

        return errors;
    }

    @Override
    public HasRoleResponse handleBase(HasRoleRequest request) {
        Subject currentUser = SecurityUtils.getSubject();

        boolean hasRole = currentUser.hasRole(request.RoleName);

        HasRoleResponse response = createResponse();

        response.HasRole = hasRole;

        return response;
    }

    @Override
    public HasRoleResponse createResponse() {
        return new HasRoleResponse();
    }
}
