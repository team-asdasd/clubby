package api.contracts.users;

import api.contracts.base.BaseResponse;
import api.contracts.dto.FormInfoDto;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;

public class GetUserInfoResponse extends BaseResponse {
    public int id;
    public String name;
    public String email;
    public boolean online;

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    public String picture;

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    public List<FormInfoDto> fields;

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    public List<String> roles;

}
