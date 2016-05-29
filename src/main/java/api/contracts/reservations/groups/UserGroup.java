package api.contracts.reservations.groups;

public class UserGroup{
    public UserGroup(int userId, int daysCount){
        this.userId = userId;
        this.daysCount = daysCount;
    }
    public int userId;
    public int daysCount;
}