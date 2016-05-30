package api.contracts.dto;

import api.business.entities.User;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class SlimUserDto {
    public int id;
    public String name;
    public String email;
    public boolean online;

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    public String picture;

    public SlimUserDto(User u) {
        id = u.getId();
        name = u.getName();
        email = u.getLogin().getEmail();
        picture = u.getPicture();
        online = u.isOnline();
    }
}
