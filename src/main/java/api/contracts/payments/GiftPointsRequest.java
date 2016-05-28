package api.contracts.payments;

import api.contracts.base.BaseRequest;

public class GiftPointsRequest extends BaseRequest {
    public int user;
    public int amount;
    public String reason;
}
