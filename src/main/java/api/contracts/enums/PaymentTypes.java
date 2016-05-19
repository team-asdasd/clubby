package api.contracts.enums;

public enum PaymentTypes {
    pay(1),
    buy(2),
    free(3);

    private final int val;
    PaymentTypes(int val) { this.val = val; }
    public int getValue() { return val; }
}
