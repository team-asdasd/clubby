package api.contracts.cottages;

import api.business.entities.Cottage;
import api.contracts.base.BaseResponse;

import java.util.List;

public class GetCottagesResponse extends BaseResponse {
    public List<Cottage> Cottages;
}
