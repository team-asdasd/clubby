package api.business.services.interfaces;

public interface IRecommendationService {
    void recommend(String recommendationCode);
    void sendRecommendationRequest(String email);
}
