package api.handlers.Recommendation;

import api.business.services.RecommendationService;
import api.business.services.interfaces.IRecommendationService;
import api.contracts.requests.GetRecommendationsRequestsRequest;
import api.contracts.responses.GetRecommendationsResponse;
import api.contracts.base.ErrorCodes;
import api.contracts.base.ErrorDto;
import api.handlers.base.BaseHandler;
import api.helpers.Validator;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;

@Stateless
public class GetRecommendationsHandler extends BaseHandler<GetRecommendationsRequestsRequest, GetRecommendationsResponse> {
    @Inject
    private IRecommendationService recommendationService;

    @Override
    public ArrayList<ErrorDto> validate(GetRecommendationsRequestsRequest request) {
        ArrayList<ErrorDto> errors = Validator.checkAllNotNullAndIsAuthenticated(request);

        return errors;
    }

    @Override
    public GetRecommendationsResponse handleBase(GetRecommendationsRequestsRequest request) {
        GetRecommendationsResponse response = createResponse();

        response.requests = recommendationService.getAllRecommendationRequests();
        return response;
    }

    @Override
    public GetRecommendationsResponse createResponse() {
        return new GetRecommendationsResponse();
    }
}
