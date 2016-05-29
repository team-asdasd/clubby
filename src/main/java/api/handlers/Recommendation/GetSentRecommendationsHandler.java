package api.handlers.Recommendation;

import api.business.services.interfaces.IRecommendationService;
import api.contracts.base.ErrorCodes;
import api.contracts.base.ErrorDto;
import api.contracts.recommendations.GetRecommendationsRequest;
import api.contracts.recommendations.GetRecommendationsResponse;
import api.handlers.base.BaseHandler;
import api.helpers.validator.Validator;
import org.apache.shiro.SecurityUtils;

import javax.inject.Inject;
import java.util.ArrayList;

public class GetSentRecommendationsHandler extends BaseHandler<GetRecommendationsRequest, GetRecommendationsResponse> {
    @Inject
    private IRecommendationService recommendationService;

    @Override
    public ArrayList<ErrorDto> validate(GetRecommendationsRequest request) {

        return new Validator().isAuthenticated().getErrors();
    }

    @Override
    public GetRecommendationsResponse handleBase(GetRecommendationsRequest request) {
        GetRecommendationsResponse response = createResponse();

        response.requests = recommendationService.getSentRecommendationRequests();
        return response;
    }

    @Override
    public GetRecommendationsResponse createResponse() {
        return new GetRecommendationsResponse();
    }
}
