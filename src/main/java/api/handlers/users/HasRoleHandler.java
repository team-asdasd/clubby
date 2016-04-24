package api.handlers.users;

import api.contracts.users.HasRoleRequest;
import api.contracts.users.HasRoleResponse;
import api.contracts.base.ErrorCodes;
import api.contracts.base.ErrorDto;
import api.handlers.base.BaseHandler;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import java.util.ArrayList;

public class HasRoleHandler extends BaseHandler<HasRoleRequest, HasRoleResponse> {
    @Override
    public ArrayList<ErrorDto> validate(HasRoleRequest request) {
        Subject currentUser = SecurityUtils.getSubject();

        ArrayList<ErrorDto> errors = new ArrayList<>();

        if (request == null) {
            errors.add(new ErrorDto("Request missing.", ErrorCodes.VALIDATION_ERROR));
            return errors;
        }

        if(request.RoleName == null){
            errors.add(new ErrorDto("RoleName missing.", ErrorCodes.VALIDATION_ERROR));
            return errors;
        }

        if(request.RoleName.isEmpty()){
            errors.add(new ErrorDto("RoleName empty.", ErrorCodes.VALIDATION_ERROR));
            return errors;
        }

        if (!currentUser.isAuthenticated()) {
            errors.add(new ErrorDto("Not authenticated.", ErrorCodes.AUTHENTICATION_ERROR));
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
