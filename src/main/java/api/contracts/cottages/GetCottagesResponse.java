package api.contracts.cottages;

import api.contracts.base.BaseResponse;
import api.contracts.dto.CottageDto;

import java.util.List;

public class GetCottagesResponse extends BaseResponse {
    public List<CottageDto> Cottages;
}
