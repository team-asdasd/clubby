package api.business.services.interfaces;

import api.business.entities.User;
import java.util.List;

public interface IGroupsAssignmentService {
    void Assign(List<User> users);
}
