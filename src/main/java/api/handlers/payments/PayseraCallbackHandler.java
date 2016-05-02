package api.handlers.payments;

import api.business.entities.MoneyTransaction;
import api.business.entities.Payment;
import api.business.entities.PaymentInfoDto;
import api.business.entities.TransactionStatus;
import api.business.services.interfaces.IPaymentsService;
import api.contracts.requests.GetPaymentInfoRequest;
import api.contracts.requests.PayseraCallbackRequest;
import api.contracts.responses.GetPaymentInfoResponse;
import api.contracts.responses.PayseraCallbackResponse;
import api.contracts.responses.base.ErrorCodes;
import api.contracts.responses.base.ErrorDto;
import api.handlers.base.BaseHandler;
import api.helpers.Parser;
import api.helpers.Validator;
import api.models.payments.PayseraCallbackParams;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;

/**
 * Created by Mindaugas on 01/05/2016.
 */
@Stateless
public class PayseraCallbackHandler extends BaseHandler<PayseraCallbackRequest, PayseraCallbackResponse> {

    @Inject
    private IPaymentsService paymentsService;

    @Override
    public ArrayList<ErrorDto> validate(PayseraCallbackRequest request) {
        ArrayList<ErrorDto> errors = Validator.checkAllNotNull(request);

        if(errors.size() == 0 && !paymentsService.checkWithMd5(request.data,request.ss1)){
            errors.add(new ErrorDto("Bad signed data from paysera.", ErrorCodes.VALIDATION_ERROR));

            logger.warn(String.format("{data:%s,ss1:%s,ss2:%s}",request.data,request.ss1,request.ss2));
        }

        return errors;
    }

    @Override
    public PayseraCallbackResponse handleBase(PayseraCallbackRequest request) {
        logger.info(String.format("paysera callback: {data:%s,ss1:%s,ss2:%s}",request.data,request.ss1,request.ss2));

        PayseraCallbackResponse response = createResponse();
        String decoded = paymentsService.decodePayseraData(request.data);
        PayseraCallbackParams callbackParams = Parser.fromQueryString(decoded, PayseraCallbackParams.class);

        MoneyTransaction mt = paymentsService.getMoneyTransaction(callbackParams.Orderid);

        if(mt == null){
            response.Errors.add(new ErrorDto(String.format("Order not found. id : %s", callbackParams.Orderid), ErrorCodes.VALIDATION_ERROR));
            return response;
        }

        if(callbackParams.Amount != mt.getAmmount()){
            response.Errors.add(new ErrorDto(String.format("Not equal money amount. Paysera : %s, clubby : %s"
                    ,callbackParams.Amount, mt.getAmmount()), ErrorCodes.VALIDATION_ERROR));
            return response;
        }

        if(!callbackParams.Currency.equals(mt.getPayment().getSettings().getCurrency())){
            response.Errors.add(new ErrorDto(String.format("Not equal money currency. Paysera : %s, clubby : %s"
                    ,callbackParams.Amount, mt.getAmmount()), ErrorCodes.VALIDATION_ERROR));
            return response;
        }

        if(callbackParams.Status == 0){
            mt.setStatus(api.models.payments.TransactionStatus.cancelled.getValue());
            paymentsService.updateMoneyTransaction(mt);
        }

        if(callbackParams.Status != 1){
            response.Errors.add(new ErrorDto(String.format("Wrong status %s", callbackParams.Status), ErrorCodes.VALIDATION_ERROR));
            return response;
        }

        if(callbackParams.Status == 1){
            mt.setStatus(api.models.payments.TransactionStatus.approved.getValue());
            response.success = true;
        }


        //todo remove only for testing
        if(response.Errors != null){
            String resp = "";
            for(ErrorDto error : response.Errors){
                resp += error.toString();
            }
            logger.info("paysera callback fail: "+ resp);
        }else{
            logger.info("paysera callback OK");
        }

        return response;
    }

    @Override
    public PayseraCallbackResponse createResponse() {
        return new PayseraCallbackResponse();
    }
}
