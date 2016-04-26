package api.handlers.Recommendation;

import api.business.services.interfaces.IRecommendationService;
import api.contracts.requests.ConfirmRecommendationRequest;
import api.contracts.responses.ConfirmRecommendationResponse;
import api.contracts.responses.base.ErrorCodes;
import api.contracts.responses.base.ErrorDto;
import api.handlers.base.BaseHandler;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;

@Stateless
public class ConfirmRecommendationHandler extends BaseHandler<ConfirmRecommendationRequest, ConfirmRecommendationResponse> {

    @Inject
    private IRecommendationService recommendationService;

    @Override
    public ArrayList<ErrorDto> validate(ConfirmRecommendationRequest request) {
        Subject currentUser = SecurityUtils.getSubject();

        ArrayList<ErrorDto> errors = new ArrayList<>();

        if (request == null) {
            errors.add(new ErrorDto("Request missing.", ErrorCodes.VALIDATION_ERROR));
        }

        if (!currentUser.isAuthenticated()) {
            errors.add(new ErrorDto("Not authenticated.", ErrorCodes.AUTHENTICATION_ERROR));
        }
        if(request.recommendationCode.isEmpty()){
            errors.add(new ErrorDto("Bad request", ErrorCodes.VALIDATION_ERROR));
        }

        return errors;
    }

    @Override
    public ConfirmRecommendationResponse handleBase(ConfirmRecommendationRequest request) {

        ConfirmRecommendationResponse response = createResponse();

        try {
            recommendationService.ConfirmRecommendation(request.recommendationCode);
        }
        catch (Exception e){
            handleException(e);
        }

        return response;
    }

    @Override
    public ConfirmRecommendationResponse createResponse() {
        return new ConfirmRecommendationResponse();
    }
}
