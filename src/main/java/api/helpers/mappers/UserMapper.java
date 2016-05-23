package api.helpers.mappers;

import api.business.entities.User;
import api.contracts.dto.UserDto;

import javax.ejb.Stateless;

@Stateless
public class UserMapper {
    public UserDto map(User u, String defaultPic) {
        UserDto userDto = new UserDto(u);
        userDto.picture = getPicture(u, defaultPic);
        return userDto;
    }

    public String getPicture(User user, String defaultPic) {
        if (user == null) {
            throw new IllegalArgumentException("user");
        }

        String userPicture = user.getPicture();
        if (userPicture != null && !userPicture.isEmpty()) {
            return userPicture;
        }

        return defaultPic;
    }
}