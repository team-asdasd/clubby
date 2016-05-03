package api.handlers.payments;

import api.business.entities.MoneyTransaction;
import api.business.entities.Payment;
import api.business.entities.PaymentsSettings;
import api.business.entities.User;
import api.business.services.interfaces.ILoginService;
import api.business.services.interfaces.IPaymentsService;
import api.contracts.base.ErrorCodes;
import api.contracts.base.ErrorDto;
import api.contracts.payments.GetPayseraParamsRequest;
import api.contracts.payments.GetPayseraParamsResponse;
import api.handlers.base.BaseHandler;
import api.helpers.Validator;
import api.contracts.enums.TransactionStatus;
import api.contracts.enums.TransactionTypes;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.*;

/**
 * Created by Mindaugas on 29/04/2016.
 */
@Stateless
public class GetPayseraParamsHandler extends BaseHandler<GetPayseraParamsRequest,GetPayseraParamsResponse> {
    @Inject
    private IPaymentsService paymentsService;

    @Inject
    private ILoginService loginService;

    @Override
    public ArrayList<ErrorDto> validate(GetPayseraParamsRequest request) {
        Subject currentUser = SecurityUtils.getSubject();

        ArrayList<ErrorDto> errors = Validator.checkAllNotNull(request);

        if (!currentUser.isAuthenticated()) {
            errors.add(new ErrorDto("Not authenticated.", ErrorCodes.AUTHENTICATION_ERROR));
        }

        return errors;
    }

    @Override
    public GetPayseraParamsResponse handleBase(GetPayseraParamsRequest request) {
        GetPayseraParamsResponse response = createResponse();

        Subject currentUser = SecurityUtils.getSubject();
        Map<String, String> queryParams = new HashMap<>();

        Payment payment = paymentsService.getPayment(request.PaymentId);
        String username = currentUser.getPrincipal().toString();
        User user = loginService.getByUserName(username).getUser();

        MoneyTransaction mt = new MoneyTransaction();
        mt.setAmmount(payment.getAmount());
        mt.setAmmountclubby(payment.getAmount());
        mt.setStatus(TransactionStatus.pending.getValue());
        mt.setPayment(payment);
        mt.setUser(user);
        mt.setTransactiontypeid(TransactionTypes.in.getValue());
        mt.setTransactionid(UUID.randomUUID().toString());

        paymentsService.createMoneyTransaction(mt);

        PaymentsSettings paymentsSettings = payment.getSettings();
        String baseUrl = System.getenv("OPENSHIFT_GEAR_DNS");
        if(baseUrl == null){
            baseUrl = "localhost:8080";
        }
        baseUrl = "http://" + baseUrl;

        //hack to resolve potential last name
        String[] names = user.getName().split(" ");
        String firstName = names[0];
        String lastName = names[0];
        if(names.length > 1){
            String[] lastNames = Arrays.copyOfRange(names, 1, names.length);
            String.join(" ", Arrays.asList(lastNames));
        }

        queryParams.put("projectid", paymentsSettings.getProjectid());
        queryParams.put("orderid", mt.getTransactionid());
        queryParams.put("version", paymentsSettings.getVersion());
        queryParams.put("currency", paymentsSettings.getCurrency());
        queryParams.put("paytext", payment.getPaytext());
        queryParams.put("p_firstname", firstName);
        queryParams.put("p_lastname", lastName);
        queryParams.put("p_email", user.getEmail());
        queryParams.put("amount", Integer.toString(payment.getAmount()));
        queryParams.put("test", "1");
        queryParams.put("accepturl", baseUrl+"/pay/accepted");
        queryParams.put("cancelurl", baseUrl+"/pay/cancelled");
        queryParams.put("callbackurl", baseUrl+"/api/paysera/callback");

        String urlEncoded = paymentsService.encodeUrl(queryParams);
        String base64PreparedUrl = paymentsService.prepareUrlEncoded(urlEncoded);
        String md5 = paymentsService.md5WithPayseraPassword(base64PreparedUrl);

        response.Data = base64PreparedUrl;
        response.Sign = md5;

        return response;
    }

    @Override
    public GetPayseraParamsResponse createResponse() {
        return new GetPayseraParamsResponse();
    }

}
