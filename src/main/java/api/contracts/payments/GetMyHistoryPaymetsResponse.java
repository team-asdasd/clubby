package api.contracts.payments;

import api.contracts.base.BaseResponse;
import api.contracts.dto.MoneyTransactionDto;

import java.util.List;

public class GetMyHistoryPaymetsResponse extends BaseResponse {
    public List<MoneyTransactionDto> payments;
}
