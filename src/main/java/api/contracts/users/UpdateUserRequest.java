package api.contracts.users;

import api.contracts.base.BaseRequest;
import api.contracts.dto.SubmitFormDto;

import java.util.List;

public class UpdateUserRequest extends BaseRequest {
    public int id;
    public String email;
    public String name;
    public String picture;
    public String password;
    public String passwordConfirm;
    public List<SubmitFormDto> fields;
}

