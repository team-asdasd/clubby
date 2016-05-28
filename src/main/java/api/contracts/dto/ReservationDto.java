package api.contracts.dto;

import api.business.entities.Reservation;
import org.joda.time.LocalDate;

public class ReservationDto {
    public int id;
    public int status;
    public int user;
    public int cottage;
    public int payment;
    public String dateFrom;
    public String dateTo;

    public ReservationDto(Reservation reservation) {
        id = reservation.getReservationid();
        user = reservation.getUser().getId();
        cottage = reservation.getCottage().getId();

        status = reservation.getStatus();

        payment = reservation.getPayment().getPaymentid();

        dateFrom = LocalDate.fromDateFields(reservation.getDateFrom()).toString();
        dateTo = LocalDate.fromDateFields(reservation.getDateTo()).toString();
    }
}
