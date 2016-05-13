package api.contracts.dto;

import api.business.entities.User;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class UserDto {
    public int Id;
    public String Name;
    public String Username;

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    public boolean FacebookUser;

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    public String Email;

    public UserDto(User u) {
        Id = u.getId();
        Name = u.getName();
        Email = u.getEmail();
        FacebookUser = u.isFacebookUser();
        Username = u.getLogin().getUsername();
    }
}
