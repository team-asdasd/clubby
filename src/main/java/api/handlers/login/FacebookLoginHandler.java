package api.handlers.login;

import api.contracts.requests.FacebookLoginRequest;
import api.contracts.responses.FacebookLoginResponse;
import api.contracts.responses.base.ErrorCodes;
import api.contracts.responses.base.ErrorDto;
import api.handlers.base.BaseHandler;
import org.apache.shiro.SecurityUtils;
import security.shiro.facebook.FacebookToken;

import java.util.ArrayList;

public class FacebookLoginHandler extends BaseHandler<FacebookLoginRequest, FacebookLoginResponse> {
    @Override
    protected ArrayList<ErrorDto> validate(FacebookLoginRequest request) {
        ArrayList<ErrorDto> errors = new ArrayList<>();

        if (request.Code == null) {
            errors.add(new ErrorDto("Code missing.", ErrorCodes.VALIDATION_ERROR));
            return errors;
        }

        if (request.Code.isEmpty()) {
            errors.add(new ErrorDto("Code empty.", ErrorCodes.VALIDATION_ERROR));
            return errors;
        }

        return errors;
    }

    @Override
    protected FacebookLoginResponse handleBase(FacebookLoginRequest request) {
        FacebookToken facebookToken = new FacebookToken(request.Code);

        SecurityUtils.getSubject().login(facebookToken);

        return createResponse();
    }

    @Override
    protected FacebookLoginResponse createResponse() {
        return new FacebookLoginResponse();
    }
}
