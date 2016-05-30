package api.business.services.interfaces;

import api.business.entities.User;
import java.util.List;

public interface IGroupsAssignmentService {
    void assign(List<User> users);
    void assign(User user);
}
