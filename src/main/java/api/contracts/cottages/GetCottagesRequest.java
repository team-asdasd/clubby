package api.contracts.cottages;

import api.contracts.base.BaseRequest;

public class GetCottagesRequest extends BaseRequest {
    public String title;
    public int bedcount;
    public int priceFrom;
    public int priceTo;
    public String dateFrom;
    public String dateTo;
}
