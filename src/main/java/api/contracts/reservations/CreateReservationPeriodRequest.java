package api.contracts.reservations;

import api.contracts.base.BaseRequest;

public class CreateReservationPeriodRequest extends BaseRequest {
    public String from;
    public String to;
}
