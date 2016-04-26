package api.handlers.Recommendation;

import api.business.services.RecommendationService;
import api.business.services.interfaces.IRecommendationService;
import api.contracts.requests.GetRecommendationsRequestsRequest;
import api.contracts.responses.GetRecommendationsResponse;
import api.contracts.responses.base.ErrorCodes;
import api.contracts.responses.base.ErrorDto;
import api.handlers.base.BaseHandler;
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
        Subject currentUser = SecurityUtils.getSubject();

        ArrayList<ErrorDto> errors = new ArrayList<>();

        if (request == null) {
            errors.add(new ErrorDto("Request missing.", ErrorCodes.VALIDATION_ERROR));
        }

        if (!currentUser.isAuthenticated()) {
            errors.add(new ErrorDto("Not authenticated.", ErrorCodes.AUTHENTICATION_ERROR));
        }

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
