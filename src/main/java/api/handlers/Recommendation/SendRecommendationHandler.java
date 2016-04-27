package api.handlers.Recommendation;

import api.business.services.interfaces.IRecommendationService;
import api.contracts.requests.SendRecommendationRequest;
import api.contracts.responses.SendRecommendationResponse;
import api.contracts.responses.base.ErrorCodes;
import api.contracts.responses.base.ErrorDto;
import api.handlers.base.BaseHandler;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.ws.rs.BadRequestException;
import java.util.ArrayList;

@Stateless
public class SendRecommendationHandler extends BaseHandler<SendRecommendationRequest, SendRecommendationResponse> {
    @Inject
    private IRecommendationService recommendationService;

    @Override
    public ArrayList<ErrorDto> validate(SendRecommendationRequest request) {
        Subject currentUser = SecurityUtils.getSubject();

        ArrayList<ErrorDto> errors = new ArrayList<>();

        if (request == null) {
            errors.add(new ErrorDto("Request missing.", ErrorCodes.VALIDATION_ERROR));
        }

        if (!currentUser.isAuthenticated()) {
            errors.add(new ErrorDto("Not authenticated.", ErrorCodes.AUTHENTICATION_ERROR));
        }
        if (request.userId <= 0) {
            errors.add(new ErrorDto("Bad request", ErrorCodes.VALIDATION_ERROR));
        }
        return errors;
    }

    @Override
    public SendRecommendationResponse handleBase(SendRecommendationRequest request) {

        SendRecommendationResponse response = createResponse();
        try {
            recommendationService.sendRecommendationRequest(request.userId);
        } catch (Exception e) {
            Throwable er = ExceptionUtils.getRootCause(e);
            if (er instanceof BadRequestException)
               response = handleException(er.getMessage(), ErrorCodes.BAD_REQUEST);
            else
                handleException(e);
        }
        return response;
    }

    @Override
    public SendRecommendationResponse createResponse() {
        return new SendRecommendationResponse();
    }
}
