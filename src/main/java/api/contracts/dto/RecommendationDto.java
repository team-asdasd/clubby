package api.contracts.dto;

import api.business.entities.Recommendation;

public class RecommendationDto {
    public String RequestCode;
    public int UserId;

    public RecommendationDto(Recommendation r) {
        UserId = r.getUserTo().getId();
        RequestCode = r.getRecommendationCode();
    }
}
