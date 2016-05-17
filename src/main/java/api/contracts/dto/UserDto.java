package api.contracts.dto;

import api.business.entities.User;

public class UserDto {
    public int id;
    public String name;
    public String email;
    public String picture;

    public UserDto(User u) {
        id = u.getId();
        name = u.getName();
        email = u.getLogin().getUsername();
        picture = u.getPicture();
    }
}
