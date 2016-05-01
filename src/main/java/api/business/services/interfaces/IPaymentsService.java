package api.business.services.interfaces;

import api.business.entities.MoneyTransaction;
import api.business.entities.Payment;
import api.business.entities.PaymentsSettings;
import api.business.entities.TransactionStatus;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.StringUtils;

import java.util.Map;

/**
 * Created by Mindaugas on 30/04/2016.
 */
public interface IPaymentsService {
    String getPassword();

    String md5WithPayseraPassword(String input);

    String prepareUrlEncoded(String input);

    String toMd5(String input);

    String encodeUrl(Map<String,String> map);

    int createPaymentsSettings(PaymentsSettings paymentsSettings);

    PaymentsSettings updatePaymentsSettings(PaymentsSettings paymentsSettings);

    void removePaymentsSettings(PaymentsSettings paymentsSettings);

    int createPayment(Payment payment);

    Payment updatePayment(Payment payment);

    void removePayment(Payment payment);

    Payment getPayment(int id);

    TransactionStatus getTransactionStatus(int status);

    void removeTransactionStatus(TransactionStatus status);

    TransactionStatus updateTransactionStatus(TransactionStatus status);

    int createTransactionStatus(TransactionStatus status);

    String createMoneyTransaction(MoneyTransaction transaction);

    MoneyTransaction updateMoneyTransaction(MoneyTransaction transaction);

    MoneyTransaction getMoneyTransaction(StringUtils id);
}
