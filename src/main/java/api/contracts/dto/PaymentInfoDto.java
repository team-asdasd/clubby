package api.contracts.dto;

import api.business.entities.Payment;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

public class PaymentInfoDto {
    public int PaymentId;
    public int PaymentTypeId;
    public String InfoText;
    public Double Amount;
    public String Currency;
    public boolean Required;
    public List<LineItemDto> LineItems;

    public PaymentInfoDto(Payment payment){
        this.Amount = payment.calculatePrice()/100d;
        this.Currency = payment.getCurrency();
        this.InfoText = payment.getPaytext();
        this.PaymentId = payment.getPaymentid();
        this.PaymentTypeId = payment.getPaymenttypeid();
        this.Required = payment.getRequired();
        this.LineItems = payment.getLineItems().stream().map(LineItemDto::new).collect(Collectors.toList());
    }

    public PaymentInfoDto(Object object){
        Object[] payment =  (Object[])object;

        this.PaymentId = (Integer) payment[0];
        this.PaymentTypeId = (Integer) payment[1];
        this.Amount = ((BigInteger) payment[2]).intValue()/100.0;
        this.Currency = (String) payment[3];
        this.InfoText = (String) payment[4];
        this.Required = (Boolean) payment[5];
    }
    public PaymentInfoDto(){}
}
