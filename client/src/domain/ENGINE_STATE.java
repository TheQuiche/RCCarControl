package domain;

public enum ENGINE_STATE {
    FULL_REVERSE(-1),
    HALF_REVERSE(-0.5),
    QUARTER_REVERSE(-0.25),
    COASTING(0),
    QUARTER_THROTTLE(0.25),
    HALF_THROTTLE(0.5),
    FULL_THROTTLE(1);

    private double value;

    public double getValue() {
        return this.value;
    }

    private ENGINE_STATE(double value) {
        this.value = value;
    }
}
