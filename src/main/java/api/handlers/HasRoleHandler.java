package api.handlers;

import api.contracts.requests.HasRoleRequest;
import api.contracts.responses.HasRoleResponse;
import api.contracts.responses.base.ErrorCodes;
import api.contracts.responses.base.ErrorDto;
import api.handlers.base.BaseHandler;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import java.util.ArrayList;

public class HasRoleHandler extends BaseHandler<HasRoleRequest, HasRoleResponse> {
    @Override
    protected ArrayList<ErrorDto> validate(HasRoleRequest request) {
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
    protected HasRoleResponse handleBase(HasRoleRequest request) {
        Subject currentUser = SecurityUtils.getSubject();

        boolean hasRole = currentUser.hasRole(request.RoleName);

        HasRoleResponse response = createResponse();

        response.HasRole = hasRole;

        return response;
    }

    @Override
    protected HasRoleResponse createResponse() {
        return new HasRoleResponse();
    }
}
