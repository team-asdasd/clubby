package api.business.services.interfaces;

import api.business.entities.User;
import api.contracts.dto.RecommendationDto;

import javax.mail.MessagingException;
import java.util.List;

public interface IRecommendationService {
    void ConfirmRecommendation(String recommendationCode);

    void sendRecommendationRequest(String userEmail) throws MessagingException;

    List<RecommendationDto> getReceivedRecommendationRequests();

    Boolean isRequestLimitReached();

    List<RecommendationDto> getSentRecommendationRequests();
}
