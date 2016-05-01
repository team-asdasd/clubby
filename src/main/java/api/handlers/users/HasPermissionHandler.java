package api.handlers.users;

import api.contracts.requests.HasPermissionRequest;
import api.contracts.responses.HasPermissionResponse;
import api.contracts.responses.base.ErrorCodes;
import api.contracts.responses.base.ErrorDto;
import api.handlers.base.BaseHandler;
import api.helpers.Validator;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import java.util.ArrayList;

public class HasPermissionHandler extends BaseHandler<HasPermissionRequest, HasPermissionResponse> {
    @Override
    public ArrayList<ErrorDto> validate(HasPermissionRequest request) {

        ArrayList<ErrorDto> errors = Validator.checkAllNotNullAndIsAuthenticated(request);

        if(request.PermissionName.isEmpty()){
            errors.add(new ErrorDto("PermissionName empty.", ErrorCodes.VALIDATION_ERROR));
            return errors;
        }

        return errors;
    }

    @Override
    public HasPermissionResponse handleBase(HasPermissionRequest request) {
        Subject currentUser = SecurityUtils.getSubject();

        boolean hasPermission = currentUser.isPermitted(request.PermissionName);

        HasPermissionResponse response = createResponse();
        response.HasPermission = hasPermission;

        return response;
    }

    @Override
    public HasPermissionResponse createResponse() {
        return new HasPermissionResponse();
    }
}
