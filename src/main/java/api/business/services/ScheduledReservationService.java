package api.business.services;

import api.business.entities.Configuration;
import api.business.entities.Reservation;
import api.business.services.interfaces.ICottageService;
import api.contracts.enums.TransactionStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.UUID;

@Singleton
public class ScheduledReservationService {
    @PersistenceContext
    private EntityManager em;
    @Inject
    private ICottageService cottageService;

    private String name = getClass().getTypeName();
    private final Logger logger = LogManager.getLogger(name);

    @Schedule(minute = "*/1", hour = "*", timezone = "UTC")
    public void cancelUnpaidReservations() throws InterruptedException {
        try {
            UUID jobId = UUID.randomUUID();

            long tStart = markStart(jobId);
            List<Reservation> reservations = cottageService.getReservations();

            int threshold = getThresholdMinutes();
            reservations.stream().filter(r -> shouldBeCanceled(r, threshold)).forEach(reservation -> {
                boolean cancelled = cottageService.cancelReservation(reservation.getReservationid());
                if (cancelled) {
                    logger.info(String.format("Canceled reservation %d", reservation.getReservationid()));
                }
            });

            markEnd(jobId, tStart);
        } catch (Exception e) {
            logger.error(String.format("Job %s failed (%s).", name, e.getMessage()));
        }
    }

    private boolean shouldBeCanceled(Reservation reservation, int threshold) {
        DateTime createdAt = new DateTime(reservation.getCreated());
        boolean cancelled = reservation.getCancelled();
        int status = reservation.getStatus();

        return !cancelled
                && createdAt.plusMinutes(threshold).isBefore(DateTime.now())
                && (status == TransactionStatus.pending.getValue()
                || status == TransactionStatus.failed.getValue());
    }

    private long markStart(UUID jobId) {
        logger.info(String.format("Started job %s, %s", name, jobId));
        return System.currentTimeMillis();
    }

    private void markEnd(UUID jobId, long tStart) {
        long tEnd = System.currentTimeMillis();
        long tDelta = tEnd - tStart;
        double elapsedSeconds = tDelta / 1000.0;
        logger.info(String.format("Finished job %s, %s. Duration: %f", name, jobId, elapsedSeconds));
    }

    public int getThresholdMinutes() {
        int defaultMinutes = 20;

        Configuration configuration = em.find(Configuration.class, "payment_delay_in_minutes");
        if (configuration == null) {
            return returnDefault(defaultMinutes);
        }

        String thresholdSetting = configuration.getValue();
        if (thresholdSetting == null) {
            return returnDefault(defaultMinutes);
        }

        try {
            return Integer.parseInt(thresholdSetting);
        } catch (Exception e) {
            logger.warn(String.format("Reservation payment threshold invalid (%s). Using default threshold (%d min).", e.getMessage(), defaultMinutes));
            return defaultMinutes;
        }
    }

    private int returnDefault(int defaultMinutes) {
        logger.warn(String.format("Reservation payment threshold is not set. Using default threshold (%d min).", defaultMinutes));
        return defaultMinutes;
    }
}
