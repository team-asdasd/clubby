package api.contracts.dto;

import api.business.entities.Reservation;

public class ReservationDto {
    private int id;
    private int status;
    private int user;
    private int cottage;

    public ReservationDto(Reservation reservation) {
        id = reservation.getReservationid();
        user = reservation.getUser().getId();
        cottage = reservation.getCottage().getId();
        status = reservation.getStatus();
    }
}
