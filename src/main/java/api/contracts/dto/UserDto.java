package api.contracts.dto;

import api.business.entities.Role;
import api.business.entities.User;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;
import java.util.stream.Collectors;

public class UserDto extends SlimUserDto {
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    public List<FormInfoDto> fields;

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    public List<String> roles;

    public UserDto(User u) {
        super(u);

        if (u.getFormResults() != null) {
            fields = u.getFormResults().stream().map(FormInfoDto::new).collect(Collectors.toList());
        }

        if (u.getLogin().getRoles() != null) {
            roles = u.getLogin().getRoles().stream().map(Role::getRoleName).collect(Collectors.toList());
        }
    }
}