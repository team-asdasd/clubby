package api.handlers.users;

import api.contracts.users.HasPermissionRequest;
import api.contracts.users.HasPermissionResponse;
import api.contracts.base.ErrorCodes;
import api.contracts.base.ErrorDto;
import api.handlers.base.BaseHandler;
import api.helpers.validator.Validator;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.ejb.Stateless;
import java.util.ArrayList;

@Stateless
public class HasPermissionHandler extends BaseHandler<HasPermissionRequest, HasPermissionResponse> {
    @Override
    public ArrayList<ErrorDto> validate(HasPermissionRequest request) {
        ArrayList<ErrorDto> authErrors = new Validator().isAuthenticated().getErrors();

        if (!authErrors.isEmpty()) return authErrors;

        ArrayList<ErrorDto> errors = new Validator().allFieldsSet(request).getErrors();
        if (request.permissionName.isEmpty()) {
            errors.add(new ErrorDto("permissionName empty.", ErrorCodes.VALIDATION_ERROR));
            return errors;
        }

        return errors;
    }

    @Override
    public HasPermissionResponse handleBase(HasPermissionRequest request) {
        Subject currentUser = SecurityUtils.getSubject();

        boolean hasPermission = currentUser.isPermitted(request.permissionName);

        HasPermissionResponse response = createResponse();
        response.hasPermission = hasPermission;

        return response;
    }

    @Override
    public HasPermissionResponse createResponse() {
        return new HasPermissionResponse();
    }
}
