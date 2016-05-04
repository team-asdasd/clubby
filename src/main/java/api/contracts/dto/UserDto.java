package api.contracts.dto;

import api.business.entities.User;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class UserDto {
    public String Name;

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    public String Email;

    public UserDto(User u) {
        this.Name = u.getName();
        this.Email = u.getEmail();
    }
}
