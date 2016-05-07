package api.contracts.dto;

import api.business.entities.MoneyTransaction;
import api.contracts.enums.TransactionTypes;

import java.util.Date;

public class MoneyTransactionDto {
    public String InfoText;
    public Double Amount;
    public String Currency;
    public Date CreationDate;
    public boolean Add;
    public int Status;

    public MoneyTransactionDto(){}

    public MoneyTransactionDto(MoneyTransaction mt){
        InfoText = mt.getPayment().getPaytext();
        Currency = mt.getPayment().getSettings().getCurrency();
        Amount = mt.getAmmount() / 100d;
        CreationDate = mt.getCreationTime();
        Add = mt.getTransactiontypeid() == TransactionTypes.in.getValue();
        Status = mt.getStatus();
    }

}
