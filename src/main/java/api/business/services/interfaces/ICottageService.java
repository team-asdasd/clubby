package api.business.services.interfaces;

import api.business.entities.*;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.List;

public interface ICottageService {
    void save(Cottage cottage);

    Cottage get(int id);

    List<Cottage> getByFilters(String title, int beds, String dateFrom, String dateTo, int priceFrom, int priceTo);

    boolean isCottageAvailableInPeriod(int cottageId, LocalDate from, LocalDate to);

    void delete(int id);

    List<Service> getCottageServices(int id);

    boolean isNowReservationPeriod();

    List<Reservation> getUpcomingReservations();

    List<Reservation> getPassedReservations();

    List<Reservation> getReservations();

    void saveReservationPeriod(DateTime from, DateTime to);

    List<ReservationsPeriod> getReservationPeriods(String fromDate, String toDate);

    boolean cancelReservation(int reservation);

    boolean isGroupAvailable();

    DateTime getCurrentPeriodStartDate();
}
