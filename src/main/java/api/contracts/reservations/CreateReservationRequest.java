package api.contracts.reservations;

import api.contracts.base.BaseRequest;
import api.contracts.reservations.services.ServiceSelectionDto;

import java.util.List;

public class CreateReservationRequest extends BaseRequest {
    public int cottage;
    public String from;
    public String to;
    public List<ServiceSelectionDto> services;
}
