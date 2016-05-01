package api.business.services;

import api.business.entities.MoneyTransaction;
import api.business.entities.Payment;
import api.business.entities.PaymentsSettings;
import api.business.entities.TransactionStatus;
import api.business.persistance.ISimpleEntityManager;
import api.business.services.interfaces.IPaymentsService;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.StringUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * Created by Mindaugas on 30/04/2016.
 */
@ApplicationScoped
public class PaymentsService implements IPaymentsService {

    @Inject
    private ISimpleEntityManager em;

    //region encryptions

    @Override
    public String getPassword() {
        return System.getenv("PAYSERAPASS");
    }

    @Override
    public String md5WithPayseraPassword(String input){
        String password = getPassword();

        return toMd5(input + password);
    }

    public String prepareUrlEncoded(String input){
        String base64 = new String(Base64.encodeBase64(input.getBytes()));
        base64 = base64.replace("/","_");
        base64 = base64.replace("+","-");

        return base64;
    }

    @Override
    public String toMd5(String input){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String encodeUrl(Map<String,String> map){
        final String[] url = {""};

        map.forEach((s, s2) -> {
            try {
                url[0] += s+"="+ URLEncoder.encode(s2, "UTF-8")+"&";
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });

        String encodedUrl = url[0].substring(0,url[0].length()-1);

        return encodedUrl;
    }

    //endregion

    //region payments settings

    @Override
    public int createPaymentsSettings(PaymentsSettings paymentsSettings){
        return em.insert(paymentsSettings).getPaymentsettingsid();
    }

    @Override
    public PaymentsSettings updatePaymentsSettings(PaymentsSettings paymentsSettings){
        return em.update(paymentsSettings);
    }

    @Override
    public void removePaymentsSettings(PaymentsSettings paymentsSettings){
        em.delete(paymentsSettings);
    }
    //endregion

    // region payments

    @Override
    public int createPayment(Payment payment){
        return em.insert(payment).getPaymentid();
    }

    @Override
    public Payment updatePayment(Payment payment){
        return em.update(payment);
    }

    @Override
    public void removePayment(Payment payment){
        em.delete(payment);
    }

    @Override
    public Payment getPayment(int id){
        return em.getById(Payment.class, id);
    }

    //endregion

    // region payments

    public int createTransactionStatus(TransactionStatus status){
        return em.insert(status).getStatus();
    }

    public TransactionStatus updateTransactionStatus(TransactionStatus status){
        return em.update(status);
    }

    public void removeTransactionStatus(TransactionStatus status){
        em.delete(status);
    }

    public TransactionStatus getTransactionStatus(int status){
        return em.getById(TransactionStatus.class, status);
    }

    //endregion

    // region transaction

    public String createMoneyTransaction(MoneyTransaction transaction){
        return em.insert(transaction).getTransactionid();
    }

    public MoneyTransaction updateMoneyTransaction(MoneyTransaction transaction){
        return em.update(transaction);
    }

    public MoneyTransaction getMoneyTransaction(StringUtils id){
        return em.getById(MoneyTransaction.class, id);
    }

    //endregion


}
