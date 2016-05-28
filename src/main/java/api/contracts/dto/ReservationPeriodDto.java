package api.contracts.dto;

import api.business.entities.ReservationsPeriod;

import java.util.Date;

public class ReservationPeriodDto {
    public int id;
    public Date from;
    public Date to;

    public ReservationPeriodDto(ReservationsPeriod pr){
        this.from = pr.getFromdate();
        this.to = pr.getTodate();
        this.id = pr.getReservationsperiodid();
    }
}
