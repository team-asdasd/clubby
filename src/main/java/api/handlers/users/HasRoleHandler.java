package api.handlers.users;

import api.contracts.users.HasRoleRequest;
import api.contracts.users.HasRoleResponse;
import api.contracts.base.ErrorCodes;
import api.contracts.base.ErrorDto;
import api.handlers.base.BaseHandler;
import api.helpers.validator.Validator;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.ejb.Stateless;
import java.util.ArrayList;

@Stateless
public class HasRoleHandler extends BaseHandler<HasRoleRequest, HasRoleResponse> {
    @Override
    public ArrayList<ErrorDto> validate(HasRoleRequest request) {
        ArrayList<ErrorDto> authErrors = new Validator().isAuthenticated().getErrors();

        if (!authErrors.isEmpty()) return authErrors;

        ArrayList<ErrorDto> errors = new Validator().allFieldsSet(request).getErrors();
        if (request.roleName.isEmpty()) {
            errors.add(new ErrorDto("roleName empty.", ErrorCodes.VALIDATION_ERROR));
            return errors;
        }

        return errors;
    }

    @Override
    public HasRoleResponse handleBase(HasRoleRequest request) {
        Subject currentUser = SecurityUtils.getSubject();

        boolean hasRole = currentUser.hasRole(request.roleName);

        HasRoleResponse response = createResponse();

        response.hasRole = hasRole;

        return response;
    }

    @Override
    public HasRoleResponse createResponse() {
        return new HasRoleResponse();
    }
}
