package api.business.services;

import api.business.entities.*;
import api.business.entities.TransactionStatus;
import api.business.persistance.ISimpleEntityManager;
import api.business.services.interfaces.IPaymentsService;
import api.contracts.dto.PaymentInfoDto;
import api.contracts.enums.*;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Stateless
public class PaymentsService implements IPaymentsService {

    @Inject
    private ISimpleEntityManager em;

    //region encryptions

    public boolean checkWithMd5(String data, String ss1) {
        String md5 = md5WithPayseraPassword(data);
        return md5.equals(ss1);
    }

    @Override
    public String getPassword() {
        return System.getenv("PAYSERAPASS");
    }

    @Override
    public String md5WithPayseraPassword(String input) {
        String password = getPassword();

        return toMd5(input + password);
    }

    public String prepareUrlEncoded(String input) {
        String base64 = new String(Base64.encodeBase64(input.getBytes()));
        base64 = base64.replace("/", "_");
        base64 = base64.replace("+", "-");

        return base64;
    }

    public String decodePayseraData(String data) {
        data = data.replace("_", "/");
        data = data.replace("-", "+");
        return new String(Base64.decodeBase64(data.getBytes()));
    }

    @Override
    public String toMd5(String input) {
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
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String encodeUrl(Map<String, String> map) {
        final String[] url = {""};

        map.forEach((s, s2) -> {
            try {
                url[0] += s + "=" + URLEncoder.encode(s2, "UTF-8") + "&";
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });

        String encodedUrl = url[0].substring(0, url[0].length() - 1);

        return encodedUrl;
    }

    //endregion

    //region payments settings

    @Override
    public int createPaymentsSettings(PaymentsSettings paymentsSettings) {
        return em.insert(paymentsSettings).getPaymentsettingsid();
    }

    @Override
    public PaymentsSettings updatePaymentsSettings(PaymentsSettings paymentsSettings) {
        return em.update(paymentsSettings);
    }

    @Override
    public void removePaymentsSettings(PaymentsSettings paymentsSettings) {
        em.delete(paymentsSettings);
    }
    //endregion

    // region payments

    @Override
    public int createPayment(Payment payment) {
        return em.insert(payment).getPaymentid();
    }

    @Override
    public Payment updatePayment(Payment payment) {
        return em.update(payment);
    }

    @Override
    public void removePayment(Payment payment) {
        em.delete(payment);
    }

    @Override
    public Payment getPayment(int id) {
        return em.getById(Payment.class, id);
    }

    //endregion

    // region payments

    public int createTransactionStatus(TransactionStatus status) {
        return em.insert(status).getStatus();
    }

    public TransactionStatus updateTransactionStatus(TransactionStatus status) {
        return em.update(status);
    }

    public void removeTransactionStatus(TransactionStatus status) {
        em.delete(status);
    }

    public TransactionStatus getTransactionStatus(int status) {
        return em.getById(TransactionStatus.class, status);
    }

    //endregion

    // region transaction

    public String createMoneyTransaction(MoneyTransaction transaction) {
        return em.insert(transaction).getTransactionid();
    }

    public MoneyTransaction updateMoneyTransaction(MoneyTransaction transaction) {
        return em.update(transaction);
    }

    public MoneyTransaction getMoneyTransaction(String id) {
        return em.getById(MoneyTransaction.class, id);
    }

    @Override
    public List<MoneyTransaction> getMoneyTransactionsByUserId(int id) {
        TypedQuery<MoneyTransaction> moneytransactions = em.getEntityManager().createQuery("SELECT m FROM MoneyTransaction m WHERE userid = :userid  ORDER BY creationTime DESC", MoneyTransaction.class)
                .setParameter("userid", id);

        return moneytransactions.getResultList();
    }

    //endregion

    //region clubby payments

    public int getMyCredit(int userId){
        BigInteger credit = (BigInteger)em.getEntityManager()
                .createNativeQuery("SELECT COALESCE(SUM(amount),0) FROM payment.moneytransactions INNER JOIN payment.payments p ON moneytransactions.paymentid = p.paymentid WHERE paymenttypeid = :paymentTypeId AND userid = :userId AND moneytransactions.transactiontypeid = :transactionTypeId AND moneytransactions.status = :status")
                .setParameter("userId", userId)
                .setParameter("paymentTypeId", PaymentTypes.pay.getValue())
                .setParameter("transactionTypeId", TransactionTypes.clubby.getValue())
                .setParameter("status", api.contracts.enums.TransactionStatus.approved.getValue())
                .getSingleResult();
        return credit.intValue();
    }

    public int getMyDebit(int userId){
        BigInteger debit = (BigInteger)em.getEntityManager()
                .createNativeQuery("SELECT COALESCE(SUM(amount),0) FROM payment.moneytransactions INNER JOIN payment.payments p ON moneytransactions.paymentid = p.paymentid WHERE (paymenttypeid = :paymentTypeId1 OR paymenttypeid = :paymentTypeId2) AND userid = :userId AND moneytransactions.status = :status")
                .setParameter("userId", userId)
                .setParameter("paymentTypeId1", PaymentTypes.buy.getValue())
                .setParameter("paymentTypeId2", PaymentTypes.free.getValue())
                .setParameter("status", api.contracts.enums.TransactionStatus.approved.getValue())
                .getSingleResult();
        return debit.intValue();
    }

    public int getMyBalance(int userId){
        return getMyDebit(userId) - getMyCredit(userId);
    }

    //endregion

    //region Pending payments

    public List<PaymentInfoDto> getPendingPaymentsForUser(int userId){

        Query q = em.getEntityManager().createNativeQuery("\n" +
                "\n" +
                "WITH pendingPaymentsIds AS (\n" +
                "SELECT \n" +
                "        DISTINCT p.paymentId\n" +
                "FROM payment.payments p\n" +
                "LEFT JOIN payment.pendingpayments pp ON p.paymentid = pp.paymentid\n" +
                "WHERE p.frequencyid <> 0 AND COALESCE(pp.userid, :userId) = :userId\n" +
                "\n" +
                "EXCEPT\n" +
                " \n" +
                "SELECT p.paymentId FROM payment.moneytransactions mt\n" +
                "INNER JOIN payment.payments p ON mt.paymentId = p.paymentId\n" +
                "WHERE mt.userId = :userId AND mt.Status = 4\n" +
                "GROUP BY p.paymentId, frequencyid\n" +
                "HAVING \n" +
                "(frequencyid = 1 AND MAX(COALESCE(EXTRACT(MONTH FROM creationtime),0)) = EXTRACT(MONTH FROM current_date))\n" +
                "OR (frequencyid = 2 AND MAX(COALESCE(EXTRACT(YEAR FROM creationtime),0)) = EXTRACT(YEAR FROM current_date))\n" +
                "OR (frequencyid = 3 AND MAX(creationtime) IS NOT NULL))\n" +
                "SELECT p.* FROM payment.payments p\n" +
                "INNER JOIN pendingPaymentsIds pp ON p.paymentId = pp.paymentId")
                .setParameter("userId", userId);

        List l = q.getResultList();


        List<PaymentInfoDto> a = (List<PaymentInfoDto>) l.stream().map(PaymentInfoDto::new).collect(Collectors.toList());

        return a;
    }

    //endregion
}
