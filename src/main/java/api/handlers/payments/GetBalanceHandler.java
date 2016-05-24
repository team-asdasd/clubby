package api.handlers.payments;

import api.business.entities.User;
import api.business.services.interfaces.IPaymentsService;
import api.business.services.interfaces.IUserService;
import api.contracts.base.ErrorCodes;
import api.contracts.base.ErrorDto;
import api.contracts.payments.GetBalanceRequest;
import api.contracts.payments.GetBalanceResponse;
import api.handlers.base.BaseHandler;
import api.helpers.Validator;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;

@Stateless
public class GetBalanceHandler extends BaseHandler<GetBalanceRequest, GetBalanceResponse> {

    @Inject
    private IPaymentsService paymentsService;
    @Inject
    private IUserService userService;

    @Override
    public ArrayList<ErrorDto> validate(GetBalanceRequest request) {
        return Validator.checkAllNotNullAndIsAuthenticated(request);
    }

    @Override
    public GetBalanceResponse handleBase(GetBalanceRequest request) {
        GetBalanceResponse response = createResponse();
        User user = userService.get();

        int balance = paymentsService.getMyBalance(user.getId());
        response.balance = balance / 100d;

        return response;
    }

    @Override
    public GetBalanceResponse createResponse() {
        return new GetBalanceResponse();
    }
}
