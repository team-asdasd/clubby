package api.contracts.reservations;

public enum ReservationCategory {
    all(0),
    upcoming(1);

    private final int val;

    ReservationCategory(int val) {
        this.val = val;
    }

    public int getValue() {
        return val;
    }
}
