package api.contracts.recommendations;

import api.contracts.dto.RecommendationDto;
import api.contracts.base.BaseResponse;

import java.util.List;

public class GetRecommendationsResponse extends BaseResponse {
    public List<RecommendationDto> requests;
}
