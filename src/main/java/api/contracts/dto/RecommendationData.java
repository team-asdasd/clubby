package api.contracts.dto;

public class RecommendationData {
    public String RequestCode;
    public int UserId;

    public RecommendationData(int userId, String requestCode) {
        UserId = userId;
        RequestCode = requestCode;
    }
}
