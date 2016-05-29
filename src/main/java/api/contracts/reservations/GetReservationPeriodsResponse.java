package api.contracts.reservations;

import api.contracts.base.BaseResponse;
import api.contracts.dto.ReservationPeriodDto;

import java.util.List;

public class GetReservationPeriodsResponse extends BaseResponse {
    public List<ReservationPeriodDto> reservationPeriods;
}
