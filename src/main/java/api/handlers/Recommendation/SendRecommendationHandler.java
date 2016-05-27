package api.handlers.Recommendation;

import api.business.entities.Recommendation;
import api.business.entities.User;
import api.business.services.interfaces.IRecommendationService;
import api.business.services.interfaces.IUserService;
import api.contracts.recommendations.SendRecommendationRequest;
import api.contracts.recommendations.SendRecommendationResponse;
import api.contracts.base.ErrorCodes;
import api.contracts.base.ErrorDto;
import api.handlers.base.BaseHandler;
import org.apache.shiro.SecurityUtils;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class SendRecommendationHandler extends BaseHandler<SendRecommendationRequest, SendRecommendationResponse> {
    @Inject
    private IRecommendationService recommendationService;
    @Inject
    private IUserService userService;
    @PersistenceContext
    private EntityManager em;

    @Override
    public ArrayList<ErrorDto> validate(SendRecommendationRequest request) {
        ArrayList<ErrorDto> errors = new ArrayList<>();

        if (!SecurityUtils.getSubject().isAuthenticated()) {
            errors.add(new ErrorDto("Not authenticated.", ErrorCodes.UNAUTHENTICATED));
            return errors;
        }
        if (recommendationService.isRequestLimitReached()) {
            errors.add(new ErrorDto("Request limit reached", ErrorCodes.VALIDATION_ERROR));
            return errors;
        }
        User userFrom = userService.getByEmail(request.UserEmail);
        if (userFrom == null) {
            errors.add(new ErrorDto("User with this email does not exist", ErrorCodes.VALIDATION_ERROR));
            return errors;
        }

        User userTo = userService.get();
        if (userTo.getId() == userFrom.getId()) {
            errors.add(new ErrorDto("Can't send request to yourself", ErrorCodes.VALIDATION_ERROR));
            return errors;
        }
        //check if userTo is candidate
        if (!userTo.getLogin().getRoles().stream().map(x -> x.getRoleName()).collect(Collectors.toList()).contains("candidate")) {
            errors.add(new ErrorDto("Already a member", ErrorCodes.VALIDATION_ERROR));
            return errors;
        }

        //check if userFrom is member
        if (userFrom.getLogin().getRoles().stream().map(x -> x.getRoleName()).collect(Collectors.toList()).contains("candidate")) {
            errors.add(new ErrorDto("Can't send request to candidate", ErrorCodes.VALIDATION_ERROR));
            return errors;
        }

        //check for multiple recommendations to same member
        List<Recommendation> recList = em.createQuery("SELECT r FROM Recommendation r WHERE r.userFrom = :userFrom " +
                "AND r.userTo = :userTo", Recommendation.class)
                .setParameter("userFrom", userFrom)
                .setParameter("userTo", userTo)
                .getResultList();
        if (recList.size() != 0) {
            errors.add(new ErrorDto("Can't send multiple request to same member", ErrorCodes.VALIDATION_ERROR));
        }

        return errors;
    }

    @Override
    public SendRecommendationResponse handleBase(SendRecommendationRequest request) {

        SendRecommendationResponse response = createResponse();

        try {
            recommendationService.sendRecommendationRequest(request.UserEmail);
        } catch (MessagingException e) {
            return handleException(e);
        }

        return response;
    }

    @Override
    public SendRecommendationResponse createResponse() {
        return new SendRecommendationResponse();
    }
}
