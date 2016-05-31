package api.contracts.reservations.groups;

import api.business.entities.User;

public class UserGroup{
    public UserGroup(User user, int daysCount){
        this.user = user;
        this.daysCount = daysCount;
    }
    public User user;
    public int daysCount;
}