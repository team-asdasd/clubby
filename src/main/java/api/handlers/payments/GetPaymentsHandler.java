package api.handlers.payments;

import api.business.services.interfaces.IPaymentsService;
import api.business.services.interfaces.IUserService;
import api.contracts.base.ErrorCodes;
import api.contracts.base.ErrorDto;
import api.contracts.dto.PaymentInfoDto;
import api.contracts.payments.GetPaymentsRequest;
import api.contracts.payments.GetPaymentsResponse;
import api.handlers.base.BaseHandler;
import api.helpers.Validator;
import logging.audit.Audit;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class GetPaymentsHandler extends BaseHandler<GetPaymentsRequest, GetPaymentsResponse> {

    @Inject
    private IPaymentsService paymentsService;
    @Inject
    private IUserService userService;

    @Override
    public ArrayList<ErrorDto> validate(GetPaymentsRequest request) {
        return Validator.checkAllNotNullAndIsAuthenticated(request);
    }

    @Override
    @Audit
    public GetPaymentsResponse handleBase(GetPaymentsRequest request) {
        GetPaymentsResponse response = createResponse();

        List<PaymentInfoDto> payments = paymentsService.getPaymentsByType(request.paymentTypeId);

        response.payments = payments;
        return response;
    }

    @Override
    public GetPaymentsResponse createResponse() {
        return new GetPaymentsResponse();
    }
}
