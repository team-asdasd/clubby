package api.contracts.reservations;

import api.contracts.base.BaseRequest;
import api.contracts.reservations.services.ServiceSelectionDto;

import java.util.Date;
import java.util.List;

public class CreateReservationRequest extends BaseRequest {
    public int cottage;
    public Date from;
    public Date to;
    public List<ServiceSelectionDto> services;
}
