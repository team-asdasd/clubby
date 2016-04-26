package api.business.services.interfaces;

import javax.mail.MessagingException;

public interface IRecommendationService {
    void ConfirmRecommendation(String recommendationCode);
    void sendRecommendationRequest(String email) throws MessagingException;
}
