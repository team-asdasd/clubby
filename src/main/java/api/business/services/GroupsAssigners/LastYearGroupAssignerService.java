package api.business.services.GroupsAssigners;

import api.business.entities.Configuration;
import api.business.entities.ReservationGroup;
import api.business.entities.User;
import api.business.persistance.ISimpleEntityManager;
import api.business.services.interfaces.IGroupsAssignmentService;
import api.business.services.interfaces.notifications.INotificationsService;
import api.contracts.enums.NotificationAction;
import api.contracts.enums.TransactionStatus;
import api.contracts.reservations.groups.UserGroup;
import api.helpers.DatesHelper;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class LastYearGroupAssignerService implements IGroupsAssignmentService {
    @Inject
    private ISimpleEntityManager simpleEntityManager;
    @PersistenceContext
    private EntityManager em;
    @Inject
    private INotificationsService notificationsService;
    private final String groupsSizeKey = "groups_count";
    private final String groupAssignedNotification = "Reservation group %d has been assigned";

    @Override
    public void assign(List<User> users) {
        int groupsSize = Integer.parseInt(simpleEntityManager.getById(Configuration.class, groupsSizeKey).getValue());
        int generationNumber = getLastGenerationNumber() + 1;

        List<UserGroup> sorted = users.stream().filter(u -> isMember(u)).map(u -> new UserGroup(u, getTotalVacationDays(u)))
                .sorted((o1, o2) -> o1.daysCount - o2.daysCount).collect(Collectors.toList());

        int step = sorted.size() <= groupsSize ? 1 : sorted.size() / groupsSize;

        if (sorted.size() > groupsSize && sorted.size() % groupsSize > 0) {
            step++;
        }

        for (int i = 0; i < sorted.size(); i++) {
            UserGroup group = sorted.get(i);
            ReservationGroup rg = new ReservationGroup(group.user, generationNumber, i / step + 1);
            simpleEntityManager.insert(rg);
        }
    }

    @Override
    public void assign(User user) {
        int generationNumber = getLastGenerationNumber();
        Configuration c = em.find(Configuration.class, groupsSizeKey);
        int groupNumber = c == null ? 1 : Integer.parseInt(c.getValue());
        simpleEntityManager.insert(new ReservationGroup(user, generationNumber, groupNumber));

        notificationsService.create(String.format(groupAssignedNotification, groupNumber ), NotificationAction.NOACTION, user.getId(), null);
    }

    private int getLastGenerationNumber() {
        Integer max = (Integer) em.createNativeQuery("SELECT MAX(generation) FROM main.reservationgroups").getSingleResult();
        return max == null ? 0 : max.intValue();
    }

    private int getTotalVacationDays(User user) {
        int vacationDays = user.getReservations().stream()
                .filter(f -> DatesHelper.inPastYear(f.getDateFrom()) &&
                        f.getPayment().getTransactions().stream()
                                .filter(mf -> mf.getStatus() == TransactionStatus.approved.getValue()).findAny().isPresent())
                .map(r -> (r.getDateFrom().getTime() - r.getDateTo().getTime()) / (24 * 60 * 60 * 1000))
                .mapToInt(Long::intValue).sum();

        return vacationDays;
    }
    private boolean isMember(User u) {
        return u.getLogin().getRoles().stream().anyMatch(r -> r.getRoleName().equals("member"));
    }
}
