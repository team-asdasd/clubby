package api.contracts.dto;

public class RecommendationDto {
    public String RequestCode;
    public int UserId;

    public RecommendationDto(int userId, String requestCode) {
        UserId = userId;
        RequestCode = requestCode;
    }
}
