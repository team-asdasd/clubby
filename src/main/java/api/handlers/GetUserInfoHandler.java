package api.handlers;

import api.contracts.requests.GetUserInfoRequest;
import api.contracts.responses.GetUserInfoResponse;
import api.handlers.base.BaseHandler;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

public class GetUserInfoHandler extends BaseHandler<GetUserInfoRequest, GetUserInfoResponse> {

    @Override
    protected boolean IsValid(GetUserInfoRequest request) {
        Subject currentUser = SecurityUtils.getSubject();

        return currentUser.isAuthenticated();
    }

    @Override
    protected GetUserInfoResponse HandleBase(GetUserInfoRequest request) {
        Subject currentUser = SecurityUtils.getSubject();

        GetUserInfoResponse response = new GetUserInfoResponse();

        response.Username = currentUser.getPrincipal().toString();

        return response;
    }

    @Override
    protected GetUserInfoResponse HandleError(Exception e) {
        return null;
    }
}
