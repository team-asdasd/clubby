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
        Currency = mt.getCurrency();
        Amount = mt.getAmount() / 100.0;
        CreationDate = mt.getCreationTime();
        Status = mt.getStatus();
    }

}
