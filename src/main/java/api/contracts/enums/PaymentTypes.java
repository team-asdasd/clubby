package api.contracts.enums;

/**
 * Created by Mindaugas on 30/04/2016.
 */
public enum PaymentTypes {
    direct(1),
    clubby(2),
    buycb(3),
    free(4);

    private final int val;
    PaymentTypes(int val) { this.val = val; }
    public int getValue() { return val; }
}
