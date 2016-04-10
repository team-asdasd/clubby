package api.contracts.responses;

import api.contracts.responses.base.BaseResponse;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class GetUserInfoResponse extends BaseResponse {
    public String Name;
    public String Email;

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    public String Picture;
}
