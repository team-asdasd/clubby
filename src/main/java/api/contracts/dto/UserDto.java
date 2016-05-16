package api.contracts.dto;

import api.business.entities.User;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class UserDto {
    public int Id;
    public String Name;
    public String Email;
    public String Picture;

    public UserDto(User u) {
        Id = u.getId();
        Name = u.getName();
        Email = u.getLogin().getUsername();
        Picture = ""; // TODO: Add picture
    }
}
