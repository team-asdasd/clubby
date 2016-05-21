package api.contracts.payments;

import api.contracts.base.BaseResponse;
import api.contracts.dto.PaymentInfoDto;
import java.util.List;

public class GetPendingPaymentsResponse extends BaseResponse {
    public List<PaymentInfoDto> pendingPayments;
}
