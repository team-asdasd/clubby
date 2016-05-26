package api.contracts.enums;

public enum PaymentsFrequency {
    any(0),
    monthly(1),
    yearly(2),
    once(3);

    private final int val;
    PaymentsFrequency(int val) { this.val = val; }
    public int getValue() { return val; }
}
