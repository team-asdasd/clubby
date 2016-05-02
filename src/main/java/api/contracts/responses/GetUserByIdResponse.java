package api.contracts.responses;

import api.contracts.base.BaseResponse;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class GetUserByIdResponse extends BaseResponse{
    public String Name;

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    public String Picture;
}
