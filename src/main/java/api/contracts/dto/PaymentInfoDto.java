package api.contracts.dto;

import api.business.entities.Payment;

public class PaymentInfoDto {
    public int PaymentId;
    public int PaymentTypeId;
    public String InfoText;
    public Double Amount;
    public String Currency;
    public boolean Required;

    public PaymentInfoDto(Payment payment){
        this.Amount = payment.getAmount()/100d;
        this.Currency = payment.getCurrency();
        this.InfoText = payment.getPaytext();
        this.PaymentId = payment.getPaymentid();
        this.PaymentTypeId = payment.getPaymenttypeid();
        this.Required = payment.getRequired();
    }

    public PaymentInfoDto(Object object){
        Object[] payment =  (Object[])object;

        this.PaymentId = (Integer) payment[0];
        this.PaymentTypeId = (Integer) payment[1];
        this.Amount = (Integer) payment[2]/100.0;
        this.Currency = (String) payment[3];
        this.InfoText = (String) payment[4];
        this.Required = (Boolean) payment[7];
    }
    public PaymentInfoDto(){}
}
