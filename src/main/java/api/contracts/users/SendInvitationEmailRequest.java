package api.contracts.users;

import api.contracts.base.BaseRequest;

public class SendInvitationEmailRequest extends BaseRequest{
    public String email;
    public String message;
}
