package api.contracts.reservations;

import api.contracts.base.BaseRequest;

public class GetReservationPeriodsRequest extends BaseRequest {
    public String from;
    public String to;
}
