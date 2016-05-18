package api.handlers.Recommendation;

import api.business.services.interfaces.IRecommendationService;
import api.contracts.recommendations.GetRecommendationsRequest;
import api.contracts.recommendations.GetRecommendationsResponse;
import api.contracts.base.ErrorDto;
import api.handlers.base.BaseHandler;
import api.helpers.Validator;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;

@Stateless
public class GetReceivedRecommendationsHandler extends BaseHandler<GetRecommendationsRequest, GetRecommendationsResponse> {
    @Inject
    private IRecommendationService recommendationService;

    @Override
    public ArrayList<ErrorDto> validate(GetRecommendationsRequest request) {
        ArrayList<ErrorDto> errors = Validator.checkAllNotNullAndIsAuthenticated(request);

        return errors;
    }

    @Override
    public GetRecommendationsResponse handleBase(GetRecommendationsRequest request) {
        GetRecommendationsResponse response = createResponse();
        try {
            response.requests = recommendationService.getReceivedRecommendationRequests();
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
