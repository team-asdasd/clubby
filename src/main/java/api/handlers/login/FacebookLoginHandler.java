package api.handlers.login;

import api.contracts.login.FacebookLoginRequest;
import api.contracts.login.FacebookLoginResponse;
import api.contracts.base.ErrorCodes;
import api.contracts.base.ErrorDto;
import api.handlers.base.BaseHandler;
import org.apache.shiro.SecurityUtils;
import security.shiro.facebook.FacebookToken;

import java.util.ArrayList;

public class FacebookLoginHandler extends BaseHandler<FacebookLoginRequest, FacebookLoginResponse> {
    @Override
    public ArrayList<ErrorDto> validate(FacebookLoginRequest request) {
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
    public FacebookLoginResponse handleBase(FacebookLoginRequest request) {
        FacebookToken facebookToken = new FacebookToken(request.Code);

        SecurityUtils.getSubject().login(facebookToken);

        return createResponse();
    }

    @Override
    public FacebookLoginResponse createResponse() {
        return new FacebookLoginResponse();
    }
}
