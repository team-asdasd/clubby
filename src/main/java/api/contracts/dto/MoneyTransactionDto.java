package api.contracts.dto;

import api.business.entities.MoneyTransaction;

import java.util.Date;

public class MoneyTransactionDto {
    public String InfoText;
    public Double Amount;
    public String Currency;
    public Date CreationDate;
    public int Status;

    public MoneyTransactionDto(){}

    public MoneyTransactionDto(MoneyTransaction mt){
        InfoText = mt.getPayment().getPaytext();
        Currency = mt.getPayment().getCurrency();
        Amount = mt.getPayment().getAmount() / 100d;
        CreationDate = mt.getCreationTime();
        Status = mt.getStatus();
    }

}