package api.contracts.payments;

import api.contracts.base.BaseResponse;
import api.contracts.dto.PaymentInfoDto;

import java.util.List;

public class GetPaymentsResponse extends BaseResponse {
    public List<PaymentInfoDto> payments;
}
