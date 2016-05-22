package api.contracts.enums;

public enum TransactionStatus {
    pending(1),
    cancelled(2),
    failed(3),
    approved(4);

    private final int val;
    TransactionStatus(int val) { this.val = val; }
    public int getValue() { return val; }
}
