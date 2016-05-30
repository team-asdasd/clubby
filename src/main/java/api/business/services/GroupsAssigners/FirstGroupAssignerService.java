package api.business.services.GroupsAssigners;

import api.business.entities.Configuration;
import api.business.entities.ReservationGroup;
import api.business.entities.User;
import api.business.persistance.ISimpleEntityManager;
import api.business.services.interfaces.IGroupsAssignmentService;

import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Alternative
public class FirstGroupAssignerService implements IGroupsAssignmentService {
    @Inject
    ISimpleEntityManager simpleEntityManager;
    @PersistenceContext
    private EntityManager em;

    @Override
    public void assign(List<User> users) {
        int generationNumber = getLastGenerationNumber() +1;

        simpleEntityManager.getAll(User.class).stream().forEach(f ->
                simpleEntityManager.insert(new ReservationGroup(f.getId(), generationNumber, 1)));

    }

    @Override
    public void assign(User user) {
        int generationNumber = getLastGenerationNumber();
        Configuration c = em.find(Configuration.class, "groups_count");
        em.persist(new ReservationGroup(user.getId(), generationNumber, c == null ? 1 : Integer.parseInt(c.getValue())));
    }

    private int getLastGenerationNumber(){
        Integer max = (Integer)em.createNativeQuery("SELECT MAX(generation) FROM main.reservationgroups").getSingleResult();
        return max == null ? 0 : max.intValue();
    }
}
