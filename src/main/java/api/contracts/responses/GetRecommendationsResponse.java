package api.contracts.responses;

import api.contracts.dto.RecommendationDTO;
import api.contracts.responses.base.BaseResponse;

import java.util.List;

public class GetRecommendationsResponse extends BaseResponse {
    public List<RecommendationDTO> requests;
}
