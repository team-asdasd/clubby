package api.handlers;

import api.contracts.requests.HasPermissionRequest;
import api.contracts.responses.HasPermissionResponse;
import api.handlers.base.BaseHandler;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

public class HasPermissionHandler extends BaseHandler<HasPermissionRequest, HasPermissionResponse> {
    @Override
    protected boolean isValid(HasPermissionRequest request) {
        Subject currentUser = SecurityUtils.getSubject();

        return currentUser.isAuthenticated();
    }

    @Override
    protected HasPermissionResponse handleBase(HasPermissionRequest request) {
        Subject currentUser = SecurityUtils.getSubject();

        boolean hasPermission = currentUser.isPermitted(request.PermissionName);

        HasPermissionResponse response = createResponse();
        response.HasPermission = hasPermission;

        return response;
    }

    @Override
    protected HasPermissionResponse createResponse() {
        return new HasPermissionResponse();
    }
}
