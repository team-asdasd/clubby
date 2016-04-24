package api.contracts.users;

import api.contracts.base.BaseRequest;

/**
 * Created by Mindaugas on 23/04/2016.
 */
public class CreateUserRequest extends BaseRequest {
    public String userName;
    public String email;
    public String firstName;
    public String lastName;
    public String password;
    public String passwordConfirm;
}
