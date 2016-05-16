package api.contracts.responses;

import api.contracts.base.BaseResponse;
import api.contracts.dto.FormInfoDto;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;

public class GetUserByIdResponse extends BaseResponse{
    public String Name;

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    public String Picture;
    public List<FormInfoDto> formInfo;
}
