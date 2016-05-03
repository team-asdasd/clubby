package api.contracts.enums;

/**
 * Created by Mindaugas on 30/04/2016.
 */
public enum TransactionTypes {
    in(1),
    out(2);

    private final int val;
    TransactionTypes(int val) { this.val = val; }
    public int getValue() { return val; }
}
