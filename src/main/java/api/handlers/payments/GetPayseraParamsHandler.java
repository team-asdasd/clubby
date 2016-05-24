package api.handlers.payments;

import api.business.entities.MoneyTransaction;
import api.business.entities.Payment;
import api.business.entities.PaymentsSettings;
import api.business.entities.User;
import api.business.services.interfaces.ILoginService;
import api.business.services.interfaces.IPaymentsService;
import api.business.services.interfaces.IUserService;
import api.contracts.base.ErrorCodes;
import api.contracts.base.ErrorDto;
import api.contracts.enums.PaymentTypes;
import api.contracts.enums.TransactionTypes;
import api.contracts.payments.GetPayseraParamsRequest;
import api.contracts.payments.GetPayseraParamsResponse;
import api.handlers.base.BaseHandler;
import api.helpers.Validator;
import api.contracts.enums.TransactionStatus;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.*;

@Stateless
public class GetPayseraParamsHandler extends BaseHandler<GetPayseraParamsRequest, GetPayseraParamsResponse> {
    @Inject
    private IPaymentsService paymentsService;

    @Inject
    private IUserService userService;

    @Override
    public ArrayList<ErrorDto> validate(GetPayseraParamsRequest request) {
        return Validator.checkAllNotNullAndIsAuthenticated(request);
    }

    @Override
    public GetPayseraParamsResponse handleBase(GetPayseraParamsRequest request) {
        GetPayseraParamsResponse response = createResponse();

        Map<String, String> queryParams = new HashMap<>();

        Payment payment = paymentsService.getPayment(request.PaymentId);

        if(payment == null){
            response.Errors = new ArrayList<>();
            response.Errors.add(new ErrorDto(String.format("Payment %s not found",request.PaymentId), ErrorCodes.NOT_FOUND));
            return response;
        }

        if(payment.getPaymenttypeid() == PaymentTypes.free.getValue()){
            response.Errors = new ArrayList<>();
            response.Errors.add(new ErrorDto("This is free payment", ErrorCodes.VALIDATION_ERROR));
            return response;
        }

        User user = userService.get();
        PaymentsSettings paymentsSettings = payment.getSettings();

        if(paymentsSettings == null){
            response.Errors = new ArrayList<>();
            response.Errors.add(new ErrorDto(String.format("Payment %s does not have payments settings"
                    ,request.PaymentId), ErrorCodes.VALIDATION_ERROR));
            return response;
        }

        MoneyTransaction mt = new MoneyTransaction();
        mt.setStatus(TransactionStatus.pending.getValue());
        mt.setPayment(payment);
        mt.setUser(user);
        mt.setTransactionid(UUID.randomUUID().toString());
        mt.setCreationTime(new Date());
        mt.setTransactionTypeId(TransactionTypes.direct.getValue());
        mt.setAmount(payment.calculatePrice());
        mt.setCurrency(payment.getCurrency());

        paymentsService.createMoneyTransaction(mt);

        String baseUrl = System.getenv("OPENSHIFT_GEAR_DNS");
        if (baseUrl == null) {
            baseUrl = "localhost:8080";
        }
        baseUrl = "http://" + baseUrl;

        //hack to resolve potential last name
        String[] names = user.getName().split(" ");
        String firstName = names[0];
        String lastName = names[0];
        if (names.length > 1) {
            String[] lastNames = Arrays.copyOfRange(names, 1, names.length);
            lastName = String.join(" ", Arrays.asList(lastNames));
        }

        queryParams.put("projectid", paymentsSettings.getProjectid());
        queryParams.put("orderid", mt.getTransactionid());
        queryParams.put("version", paymentsSettings.getVersion());
        queryParams.put("currency", payment.getCurrency());
        queryParams.put("paytext", payment.getPaytext());
        queryParams.put("p_firstname", firstName);
        queryParams.put("p_lastname", lastName);
        queryParams.put("p_email", user.getLogin().getEmail());
        queryParams.put("amount", Integer.toString(payment.calculatePrice()));
        queryParams.put("test", "1");
        queryParams.put("accepturl", baseUrl + "/pay/accepted");
        queryParams.put("cancelurl", baseUrl + "/pay/cancelled");
        queryParams.put("callbackurl", baseUrl + "/api/paysera/callback");

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
