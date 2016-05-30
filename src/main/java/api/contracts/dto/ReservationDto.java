package api.contracts.dto;

import api.business.entities.Reservation;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class ReservationDto {
    public int id;
    public int status;
    public SlimUserDto user;
    public SlimCottageDto cottage;
    public int payment;
    public String dateFrom;
    public String dateTo;
    public String created;

    public ReservationDto(Reservation reservation) {
        id = reservation.getReservationid();
        user = new SlimUserDto(reservation.getUser());
        cottage = new SlimCottageDto(reservation.getCottage());

        status = reservation.getStatus();

        payment = reservation.getPayment().getPaymentid();

        created = new DateTime(reservation.getCreated()).toString();

        dateFrom = LocalDate.fromDateFields(reservation.getDateFrom()).toString();
        dateTo = LocalDate.fromDateFields(reservation.getDateTo()).toString();
    }
}