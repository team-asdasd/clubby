package api.handlers.users;

import api.business.services.EmailService;
import api.business.services.interfaces.IEmailService;
import api.business.services.interfaces.IUserService;
import api.contracts.base.BaseResponse;
import api.contracts.base.ErrorCodes;
import api.contracts.base.ErrorDto;
import api.contracts.users.SendInvitationEmailRequest;
import api.handlers.base.BaseHandler;
import api.helpers.Validator;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.MessagingException;
import java.util.ArrayList;

@Stateless
public class SendInvitationEmailHandler extends BaseHandler<SendInvitationEmailRequest, BaseResponse> {
    @Inject
    private IUserService userService;
    @Inject
    private IEmailService emailService;

    @Override
    public ArrayList<ErrorDto> validate(SendInvitationEmailRequest request) {
        ArrayList<ErrorDto> errors = Validator.checkAllNotNullAndIsAuthenticated(request);
        if (!EmailService.isValidEmailAddress(request.email)) {
            errors.add(new ErrorDto("Invalid email", ErrorCodes.INCORRECT_EMAIL));
            return errors;
        }
        if (userService.getByEmail(request.email) != null) {
            errors.add(new ErrorDto("User with this email already exist", ErrorCodes.VALIDATION_ERROR));
        }
        return errors;
    }

    @Override
    public BaseResponse handleBase(SendInvitationEmailRequest request) {
        BaseResponse response = createResponse();
        try {
            emailService.send(request.email, "Invitation to club", String.format("You have received invitation to club Labanoro draugai, from  %s\n%s\n %s",
                    userService.get().getLogin().getEmail(), request.message, System.getenv("OPENSHIFT_APP_DNS")));
        } catch (MessagingException e) {
            response.Errors.add(new ErrorDto("Error while sending email", ErrorCodes.GENERAL_ERROR));
        }
        return response;
    }

    @Override
    public BaseResponse createResponse() {
        return new BaseResponse();
    }
}
