package api.contracts.users;

import api.contracts.base.BaseResponse;
import api.contracts.dto.FormInfoDto;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;

public class GetUserInfoResponse extends BaseResponse {
    public int Id;
    public String Name;
    public String Email;

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    public String Picture;

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    public List<FormInfoDto> Fields;
}
