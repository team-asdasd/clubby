package api.contracts.payments;

import api.contracts.base.BaseResponse;
import api.contracts.dto.PaymentInfoDto;

import java.util.List;

public class GetBalanceResponse extends BaseResponse {
    public double balance;
}
