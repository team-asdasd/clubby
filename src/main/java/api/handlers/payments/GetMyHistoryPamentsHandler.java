package api.handlers.payments;

import api.business.entities.MoneyTransaction;
import api.business.entities.User;
import api.business.services.interfaces.IPaymentsService;
import api.business.services.interfaces.IUserService;
import api.contracts.base.ErrorDto;
import api.contracts.dto.MoneyTransactionDto;
import api.contracts.payments.GetMyHistoryPaymetsRequest;
import api.contracts.payments.GetMyHistoryPaymetsResponse;
import api.handlers.base.BaseHandler;
import api.helpers.validator.Validator;
import logging.audit.Audit;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class GetMyHistoryPamentsHandler extends BaseHandler<GetMyHistoryPaymetsRequest, GetMyHistoryPaymetsResponse> {
    @Inject
    private IPaymentsService paymentsService;
    @Inject
    private IUserService userService;

    @Override
    public ArrayList<ErrorDto> validate(GetMyHistoryPaymetsRequest request) {
        ArrayList<ErrorDto> authErrors = new Validator().isAuthenticated().getErrors();

        if (!authErrors.isEmpty()) return authErrors;

        return new Validator().isAdministrator().allFieldsSet(request).getErrors();
    }

    @Override
    @Audit
    public GetMyHistoryPaymetsResponse handleBase(GetMyHistoryPaymetsRequest request) {
        GetMyHistoryPaymetsResponse response = createResponse();
        User user = userService.get();

        List<MoneyTransaction> payments = paymentsService.getMoneyTransactionsByUserId(user.getId());

        response.payments = payments.stream().map(MoneyTransactionDto::new).collect(Collectors.toList());

        return response;
    }

    @Override
    public GetMyHistoryPaymetsResponse createResponse() {
        return new GetMyHistoryPaymetsResponse();
    }
}
