package api.business.services.interfaces;

import api.contracts.dto.RecommendationData;

import javax.mail.MessagingException;
import java.util.List;

public interface IRecommendationService {
    void ConfirmRecommendation(String recommendationCode);
    void sendRecommendationRequest(int userId) throws MessagingException;
    List<RecommendationData> getAllRecommendationRequests();
}
