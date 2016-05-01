package api.handlers.Recommendation;

import api.business.services.interfaces.IRecommendationService;
import api.contracts.requests.ConfirmRecommendationRequest;
import api.contracts.responses.ConfirmRecommendationResponse;
import api.contracts.responses.base.ErrorCodes;
import api.contracts.responses.base.ErrorDto;
import api.handlers.base.BaseHandler;
import api.helpers.Validator;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import java.util.ArrayList;

@Stateless
public class ConfirmRecommendationHandler extends BaseHandler<ConfirmRecommendationRequest, ConfirmRecommendationResponse> {

    @Inject
    private IRecommendationService recommendationService;

    @Override
    public ArrayList<ErrorDto> validate(ConfirmRecommendationRequest request) {
        ArrayList<ErrorDto> errors = Validator.checkAllNotNullAndIsAuthenticated(request);

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
        catch (BadRequestException e){
            handleException(e.getMessage(), ErrorCodes.BAD_REQUEST);
        }

        return response;
    }

    @Override
    public ConfirmRecommendationResponse createResponse() {
        return new ConfirmRecommendationResponse();
    }
}
