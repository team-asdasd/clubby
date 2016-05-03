package api.handlers.payments;

import api.business.entities.MoneyTransaction;
import api.business.services.interfaces.IPaymentsService;
import api.contracts.base.ErrorCodes;
import api.contracts.base.ErrorDto;
import api.contracts.enums.TransactionStatus;
import api.contracts.payments.PayseraCallbackRequest;
import api.contracts.payments.PayseraCallbackResponse;
import api.handlers.base.BaseHandler;
import api.helpers.Parser;
import api.helpers.Validator;
import api.contracts.dto.PayseraCallbackParamsDto;

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
        PayseraCallbackParamsDto callbackParams = Parser.fromQueryString(decoded, PayseraCallbackParamsDto.class);

        MoneyTransaction mt = paymentsService.getMoneyTransaction(callbackParams.Orderid);

        if(mt == null){
            response.Errors = new ArrayList<>();
            response.Errors.add(new ErrorDto(String.format("Order not found. id : %s", callbackParams.Orderid), ErrorCodes.VALIDATION_ERROR));
            return response;
        }

        if(callbackParams.Amount != mt.getAmmount()){
            response.Errors = new ArrayList<>();
            response.Errors.add(new ErrorDto(String.format("Not equal money amount. Paysera : %s, clubby : %s"
                    ,callbackParams.Amount, mt.getAmmount()), ErrorCodes.VALIDATION_ERROR));
            return response;
        }

        if(!callbackParams.Currency.equals(mt.getPayment().getSettings().getCurrency())){
            response.Errors = new ArrayList<>();
            response.Errors.add(new ErrorDto(String.format("Not equal money currency. Paysera : %s, clubby : %s"
                    ,callbackParams.Amount, mt.getAmmount()), ErrorCodes.VALIDATION_ERROR));
            return response;
        }

        if(callbackParams.Status == 0){
            mt.setStatus(TransactionStatus.cancelled.getValue());
            paymentsService.updateMoneyTransaction(mt);
        }

        if(callbackParams.Status != 1){
            response.Errors = new ArrayList<>();
            response.Errors.add(new ErrorDto(String.format("Wrong status %s", callbackParams.Status), ErrorCodes.VALIDATION_ERROR));
            return response;
        }

        if(callbackParams.Status == 1){
            mt.setStatus(TransactionStatus.approved.getValue());
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
