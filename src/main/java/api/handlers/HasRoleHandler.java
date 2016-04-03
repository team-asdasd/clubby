package api.handlers;

import api.contracts.requests.HasRoleRequest;
import api.contracts.responses.HasRoleResponse;
import api.handlers.base.BaseHandler;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

public class HasRoleHandler extends BaseHandler<HasRoleRequest, HasRoleResponse> {

    @Override
    protected boolean isValid(HasRoleRequest request) {
        Subject currentUser = SecurityUtils.getSubject();

        return currentUser.isAuthenticated();
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
