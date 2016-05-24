package api.handlers.payments;

import api.business.entities.Payment;
import api.business.entities.User;
import api.business.services.interfaces.IUserService;
import api.contracts.dto.PaymentInfoDto;
import api.business.services.interfaces.IPaymentsService;
import api.contracts.base.ErrorCodes;
import api.contracts.base.ErrorDto;
import api.contracts.payments.GetPaymentInfoRequest;
import api.contracts.payments.GetPaymentInfoResponse;
import api.handlers.base.BaseHandler;
import api.helpers.Validator;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;

@Stateless
public class GetPaymentInfoHandler extends BaseHandler<GetPaymentInfoRequest, GetPaymentInfoResponse> {

    @Inject
    private IPaymentsService paymentsService;
    @Inject
    private IUserService userService;

    @Override
    public ArrayList<ErrorDto> validate(GetPaymentInfoRequest request) {
        return Validator.checkAllNotNullAndIsAuthenticated(request);
    }

    @Override
    public GetPaymentInfoResponse handleBase(GetPaymentInfoRequest request) {
        GetPaymentInfoResponse response = createResponse();
        Payment payment = paymentsService.getPayment(request.PaymentId);

        if(payment == null){
            response.Errors = new ArrayList<>();
            response.Errors.add(new ErrorDto(String.format("Payment %s not found",request.PaymentId), ErrorCodes.NOT_FOUND));
            return response;
        }

        User user = userService.get();

        if(!payment.canAcces(user)){
            response.Errors = new ArrayList<>();
            response.Errors.add(new ErrorDto("Cant access this payment", ErrorCodes.VALIDATION_ERROR));
            return response;
        }

        PaymentInfoDto paymentInfoDto = new PaymentInfoDto(payment);

        response.paymentInfoDto = paymentInfoDto;

        return response;
    }

    @Override
    public GetPaymentInfoResponse createResponse() {
        return new GetPaymentInfoResponse();
    }
}
