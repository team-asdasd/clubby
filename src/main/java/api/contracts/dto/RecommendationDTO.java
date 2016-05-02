package api.contracts.dto;

public class RecommendationDTO {
    public String RequestCode;
    public int UserId;

    public RecommendationDTO(int userId, String requestCode) {
        UserId = userId;
        RequestCode = requestCode;
    }
}
