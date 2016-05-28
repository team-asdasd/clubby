package api.handlers.payments;

import api.business.entities.User;
import api.business.services.interfaces.IPaymentsService;
import api.business.services.interfaces.IUserService;
import api.contracts.base.ErrorDto;
import api.contracts.dto.PaymentInfoDto;
import api.contracts.payments.GetPendingPaymentsRequest;
import api.contracts.payments.GetPendingPaymentsResponse;
import api.handlers.base.BaseHandler;
import api.helpers.validator.Validator;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class GetPendingPaymentsHandler extends BaseHandler<GetPendingPaymentsRequest, GetPendingPaymentsResponse> {

    @Inject
    private IPaymentsService paymentsService;
    @Inject
    private IUserService userService;

    @Override
    public ArrayList<ErrorDto> validate(GetPendingPaymentsRequest request) {
        ArrayList<ErrorDto> authErrors = new Validator().isAuthenticated().getErrors();

        if (!authErrors.isEmpty())
            return authErrors;

        return new Validator().allFieldsSet(request).getErrors();
    }

    @Override
    public GetPendingPaymentsResponse handleBase(GetPendingPaymentsRequest request) {
        GetPendingPaymentsResponse response = createResponse();
        User user = userService.get();

        List<PaymentInfoDto> pendingPpayments = paymentsService.getPendingPaymentsForUser(user.getId());

        response.pendingPayments = pendingPpayments;
        return response;
    }

    @Override
    public GetPendingPaymentsResponse createResponse() {
        return new GetPendingPaymentsResponse();
    }
}
