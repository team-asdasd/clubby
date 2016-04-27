package api.contracts.responses;

import api.contracts.dto.RecommendationData;
import api.contracts.responses.base.BaseResponse;

import java.util.List;

public class GetRecommendationsResponse extends BaseResponse {
    public List<RecommendationData> requests;
}
