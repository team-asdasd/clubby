package api.contracts.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

public class RecommendationDto {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String requestCode;
    public int userId;
    public int status;

    public RecommendationDto(int status, int userId, String statusCode) {
        this.userId = userId;
        requestCode = statusCode;
        this.status = status;
    }
}
