package api.contracts.dto;

import api.business.entities.Recommendation;
import api.business.entities.User;
import com.fasterxml.jackson.annotation.JsonInclude;

public class RecommendationDto {
    public String email;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String requestCode;
    public int userId;
    public int status;

    public RecommendationDto(int status, User user, String statusCode) {
        this.userId = user.getId();
        this.email = user.getLogin().getEmail();
        this.requestCode = statusCode;
        this.status = status;
    }

    public RecommendationDto(Recommendation r) {
        this.userId = r.getUserTo().getId();
        this.email = r.getUserTo().getLogin().getEmail();
        this.requestCode = r.getRecommendationCode();
        this.status = r.getStatus();
    }
}
