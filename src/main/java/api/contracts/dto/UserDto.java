package api.contracts.dto;

import api.business.entities.Role;
import api.business.entities.User;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.subject.Subject;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserDto {
    public int id;
    public String name;
    public String email;
    public boolean isOnline;

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    public String picture;

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    public List<FormInfoDto> fields;

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    public List<String> roles;

    public UserDto(User u) {
        id = u.getId();
        name = u.getName();
        email = u.getLogin().getEmail();
        picture = u.getPicture();

        if (u.getFormResults() != null) {
            fields = u.getFormResults().stream().map(FormInfoDto::new).collect(Collectors.toList());
        }

        if (u.getLogin().getRoles() != null) {
            roles = u.getLogin().getRoles().stream().map(Role::getRoleName).collect(Collectors.toList());
        }
        isOnline = u.isOnline();
    }
}
