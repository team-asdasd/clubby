package api.business.services.GroupsAssigners;

import api.business.entities.Configuration;
import api.business.entities.ReservationGroup;
import api.business.entities.User;
import api.business.persistance.ISimpleEntityManager;
import api.business.services.interfaces.IGroupsAssignmentService;
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
    ISimpleEntityManager simpleEntityManager;
    @PersistenceContext
    private EntityManager em;
    private final String groupsSizeKey = "groups_count";

    @Override
    public void Assign(List<User> users) {
        int groupsSize = Integer.parseInt(simpleEntityManager.getById(Configuration.class, groupsSizeKey).getValue());
        int generationNumber = getLastGenerationNumber() +1;

        List<UserGroup> sorted = users.stream().map(u -> new UserGroup(u.getId(), getTotalVacationDays(u)))
                .sorted((o1, o2) -> o1.daysCount - o2.daysCount).collect(Collectors.toList());

        int step = sorted.size() <= groupsSize ? 1 : sorted.size()/groupsSize;

        if(sorted.size() > groupsSize && sorted.size() % groupsSize > 0){
            step++;
        }

        for(int i = 0; i < sorted.size(); i++){
            UserGroup group = sorted.get(i);
            ReservationGroup rg = new ReservationGroup(group.userId, generationNumber, i/step+1);
            simpleEntityManager.insert(rg);
        }
    }

    private int getLastGenerationNumber(){
        Integer max = (Integer)em.createNativeQuery("SELECT MAX(generation) FROM main.reservationgroups").getSingleResult();
        return max == null ? 0 : max.intValue();
    }

    private int getTotalVacationDays(User user){
        int vacationDays = user.getReservations().stream()
                .filter(f -> DatesHelper.inPastYear(f.getDateFrom()) &&
                        f.getPayment().getTransactions().stream()
                                .filter(mf -> mf.getStatus() == TransactionStatus.approved.getValue()).findAny().isPresent())
                .map(r -> (r.getDateFrom().getTime() - r.getDateTo().getTime())/ (24 * 60 * 60 * 1000))
                .mapToInt(Long::intValue).sum();

        return vacationDays;
    }
}
