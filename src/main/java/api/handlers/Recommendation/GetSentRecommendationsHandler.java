package api.handlers.Recommendation;

import api.business.services.interfaces.IRecommendationService;
import api.contracts.base.ErrorCodes;
import api.contracts.base.ErrorDto;
import api.contracts.recommendations.GetRecommendationsRequest;
import api.contracts.recommendations.GetRecommendationsResponse;
import api.handlers.base.BaseHandler;
import org.apache.shiro.SecurityUtils;

import javax.inject.Inject;
import java.util.ArrayList;

public class GetSentRecommendationsHandler extends BaseHandler<GetRecommendationsRequest, GetRecommendationsResponse> {
    @Inject
    private IRecommendationService recommendationService;

    @Override
    public ArrayList<ErrorDto> validate(GetRecommendationsRequest request) {
        ArrayList<ErrorDto> errors = new ArrayList<>();

        if (!SecurityUtils.getSubject().isAuthenticated()) {
            errors.add(new ErrorDto("Not authenticated.", ErrorCodes.AUTHENTICATION_ERROR));
            return errors;
        }
        return errors;
    }

    @Override
    public GetRecommendationsResponse handleBase(GetRecommendationsRequest request) {
        GetRecommendationsResponse response = createResponse();
        try {
            response.requests = recommendationService.getSentRecommendationRequests();
        } catch (Exception e) {
            return handleException(e);
        }
        return response;
    }

    @Override
    public GetRecommendationsResponse createResponse() {
        return new GetRecommendationsResponse();
    }
}
