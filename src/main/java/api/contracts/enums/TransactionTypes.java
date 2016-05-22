package api.contracts.enums;

public enum TransactionTypes {
    direct(1),
    clubby(2),
    free(3);

    private final int val;
    TransactionTypes(int val) { this.val = val; }
    public int getValue() { return val; }
}
