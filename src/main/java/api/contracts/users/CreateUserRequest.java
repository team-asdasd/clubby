package api.contracts.users;

import api.contracts.base.BaseRequest;
import api.contracts.dto.SubmitFormDto;

import java.util.List;

public class CreateUserRequest extends BaseRequest {
    public String email;
    public String name;
    public String password;
    public String passwordConfirm;
    public List<SubmitFormDto> fields;
}
