package api.handlers.Recommendation;

import api.business.services.interfaces.IRecommendationService;
import api.contracts.recommendations.ConfirmRecommendationRequest;
import api.contracts.recommendations.ConfirmRecommendationResponse;
import api.contracts.base.ErrorCodes;
import api.contracts.base.ErrorDto;
import api.handlers.base.BaseHandler;
import api.helpers.Validator;
import org.apache.commons.lang3.exception.ExceptionUtils;

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

        if (request.recommendationCode.isEmpty()) {
            errors.add(new ErrorDto("Bad request", ErrorCodes.BAD_REQUEST));
        }
        return errors;
    }

    @Override
    public ConfirmRecommendationResponse handleBase(ConfirmRecommendationRequest request) {

        ConfirmRecommendationResponse response = createResponse();

        try {
            recommendationService.ConfirmRecommendation(request.recommendationCode);
        } catch (Exception e) {
            Throwable er = ExceptionUtils.getRootCause(e);
            if (er instanceof BadRequestException)
                response = handleException(er.getMessage(), ErrorCodes.BAD_REQUEST);
            else
                return handleException(e);
        }
        return response;
    }

    @Override
    public ConfirmRecommendationResponse createResponse() {
        return new ConfirmRecommendationResponse();
    }
}
