package api.handlers.Recommendation;

import api.business.services.interfaces.IRecommendationService;
import api.contracts.requests.ReceiveRecommendationRequest;
import api.contracts.responses.ReceiveRecommendationResponse;
import api.contracts.responses.base.ErrorCodes;
import api.contracts.responses.base.ErrorDto;
import api.handlers.base.BaseHandler;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;

@Stateless
public class ReceiveRecommendationHandler extends BaseHandler<ReceiveRecommendationRequest, ReceiveRecommendationResponse> {

    @Inject
    private IRecommendationService recommendationService;

    @Override
    public ArrayList<ErrorDto> validate(ReceiveRecommendationRequest request) {
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
    public ReceiveRecommendationResponse handleBase(ReceiveRecommendationRequest request) {
        Subject currentUser = SecurityUtils.getSubject();
        ReceiveRecommendationResponse response = createResponse();

        recommendationService.recommend(request.recommendationCode);
        return response;
    }

    @Override
    public ReceiveRecommendationResponse createResponse() {
        return new ReceiveRecommendationResponse();
    }
}
