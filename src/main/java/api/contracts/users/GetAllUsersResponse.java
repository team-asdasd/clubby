package api.contracts.users;

import api.contracts.base.BaseResponse;
import api.contracts.dto.UserDto;

import java.util.List;

public class GetAllUsersResponse extends BaseResponse {
    public List<UserDto> Users;
}
