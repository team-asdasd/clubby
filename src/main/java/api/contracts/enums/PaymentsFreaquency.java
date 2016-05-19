package api.contracts.enums;

public enum PaymentsFreaquency {
    any(0),
    monthly(1),
    yearly(2),
    once(3);

    private final int val;
    PaymentsFreaquency(int val) { this.val = val; }
    public int getValue() { return val; }
}
