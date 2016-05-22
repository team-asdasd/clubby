package api.contracts.users;

import api.contracts.base.BaseResponse;
import api.contracts.dto.SubmitFormDto;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;

public class UpdateUserResponse extends BaseResponse {
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
    public int id;

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    public String email;

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    public String name;

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    public String picture;

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    public String password;

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    public String passwordConfirm;

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    public List<SubmitFormDto> fields;
}
