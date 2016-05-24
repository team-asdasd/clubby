package api.contracts.reservations;

import api.contracts.base.BaseResponse;
import api.contracts.dto.ReservationDto;

import java.util.List;

public class GetReservationsResponse extends BaseResponse {
    public List<ReservationDto> reservations;
}
