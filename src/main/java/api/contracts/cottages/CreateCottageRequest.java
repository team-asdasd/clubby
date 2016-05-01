package api.contracts.cottages;

import api.contracts.base.BaseRequest;

public class CreateCottageRequest extends BaseRequest{
    public String title;
    public int bedcount;
    public String imageurl;
}
