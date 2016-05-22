package api.handlers.Recommendation;

import api.business.entities.Recommendation;
import api.business.entities.User;
import api.business.services.interfaces.IRecommendationService;
import api.business.services.interfaces.IUserService;
import api.contracts.recommendations.ConfirmRecommendationRequest;
import api.contracts.recommendations.ConfirmRecommendationResponse;
import api.contracts.base.ErrorCodes;
import api.contracts.base.ErrorDto;
import api.handlers.base.BaseHandler;
import api.helpers.Validator;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.BadRequestException;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class ConfirmRecommendationHandler extends BaseHandler<ConfirmRecommendationRequest, ConfirmRecommendationResponse> {

    @Inject
    private IRecommendationService recommendationService;
    @Inject
    private IUserService userService;
    @PersistenceContext
    private EntityManager em;

    @Override
    public ArrayList<ErrorDto> validate(ConfirmRecommendationRequest request) {
        ArrayList<ErrorDto> errors = Validator.checkAllNotNullAndIsAuthenticated(request);

        if (request.recommendationCode.isEmpty()) {
            errors.add(new ErrorDto("Bad request", ErrorCodes.BAD_REQUEST));
        }

        List<Recommendation> recommendations = em.createQuery("SELECT r FROM Recommendation r JOIN r.userTo u WHERE r.recommendationCode = :recommendationCode AND r.status <> 1", Recommendation.class)
                .setParameter("recommendationCode", request.recommendationCode)
                .getResultList();

        if (recommendations.size() != 1) {
            errors.add(new ErrorDto("Bad recommendation code", ErrorCodes.VALIDATION_ERROR));
            return errors;
        }
        User userFrom = recommendations.get(0).getUserFrom();
        User currentUser = userService.get();

        if (!userFrom.getLogin().getEmail().equals(currentUser.getLogin().getEmail())) {
            errors.add(new ErrorDto("You can't confirm this request", ErrorCodes.VALIDATION_ERROR));
        }
        return errors;
    }

    @Override
    public ConfirmRecommendationResponse handleBase(ConfirmRecommendationRequest request) {

        ConfirmRecommendationResponse response = createResponse();

        recommendationService.ConfirmRecommendation(request.recommendationCode);

        return response;
    }

    @Override
    public ConfirmRecommendationResponse createResponse() {
        return new ConfirmRecommendationResponse();
    }
}
