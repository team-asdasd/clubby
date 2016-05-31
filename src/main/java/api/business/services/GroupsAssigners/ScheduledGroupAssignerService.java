package api.business.services.GroupsAssigners;

import api.business.entities.ReservationsPeriod;
import api.business.entities.User;
import api.business.services.interfaces.IGroupsAssignmentService;
import api.business.services.interfaces.notifications.INotificationsService;
import api.contracts.enums.NotificationAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Date;
import java.util.List;
import java.util.UUID;

@Singleton
public class ScheduledGroupAssignerService {
    @PersistenceContext
    private EntityManager em;
    @Inject
    private IGroupsAssignmentService assignmentService;
    @Inject
    private INotificationsService notificationsService;

    private String name = getClass().getTypeName();
    private final Logger logger = LogManager.getLogger(name);
    private final String groupsAssignedNotification = "Reservation groups has been assigned.";

    @Schedule(minute = "0", hour = "0", timezone = "Europe/Helsinki")
    public void assignGroups() throws InterruptedException {
        try {
            UUID jobId = UUID.randomUUID();
            long tStart = markStart(jobId);
            List<ReservationsPeriod> periods = em.createQuery("SELECT r FROM ReservationsPeriod r", ReservationsPeriod.class).getResultList();

            if (periods.stream().anyMatch(p -> isToday(p.getFromdate()))) {
                assignReservationGroups();
            }

            markEnd(jobId, tStart);
        } catch (Exception e) {
            logger.error(String.format("Job %s failed (%s).", name, e.getMessage()));
        }
    }

    private void assignReservationGroups() {
        List<User> users = em.createQuery("SELECT U FROM User u WHERE u.login.disabled = false", User.class).getResultList();
        assignmentService.assign(users);
        logger.info("Reservation groups has been assigned.");
        notificationsService.create(groupsAssignedNotification, NotificationAction.NOACTION.name());
    }

    private boolean isToday(Date date) {
        DateTimeZone dateTimeZone = DateTimeZone.forID("Europe/Helsinki");
        DateTime dateTime = new DateTime(date);
        LocalDate localDate = new DateTime().withZone(dateTimeZone).toLocalDate();
        return dateTime.toLocalDate().equals(localDate);
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
}
