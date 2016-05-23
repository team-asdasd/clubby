package api.contracts.reservations;

import api.business.entities.Reservation;
import api.contracts.base.BaseResponse;

import java.util.List;

public class GetReservationsResponse extends BaseResponse {
    public List<Reservation> reservations;
}
