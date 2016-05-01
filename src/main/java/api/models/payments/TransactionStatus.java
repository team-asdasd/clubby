package api.models.payments;

/**
 * Created by Mindaugas on 30/04/2016.
 */
public enum TransactionStatus {
    pending(1),
    cancelled(2),
    failed(3),
    approved(4);

    private final int val;
    TransactionStatus(int val) { this.val = val; }
    public int getValue() { return val; }
}
