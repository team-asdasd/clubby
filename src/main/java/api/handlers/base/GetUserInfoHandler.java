package api.handlers.base;

import api.contracts.requests.base.BaseRequest;
import api.contracts.responses.UserResponse;
import api.contracts.responses.base.BaseResponse;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

public class GetUserInfoHandler<TRequest extends BaseRequest, TResponse extends BaseResponse> extends BaseHandler<TRequest, TResponse> {
    @Override
    protected boolean IsValid(TRequest request) {
        Subject currentUser = SecurityUtils.getSubject();

        return currentUser.isAuthenticated();

    }

    @Override
    protected TResponse HandleBase(TRequest request) {
        Subject currentUser = SecurityUtils.getSubject();

        UserResponse response = new UserResponse();

        response.Username = currentUser.getPrincipal().toString();

        return (TResponse) response;
    }

    @Override
    protected TResponse HandleError(Exception e) {
        return null;
    }
}
