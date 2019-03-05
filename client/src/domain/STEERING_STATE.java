package domain;

public enum STEERING_STATE {
    FULL_LEFT(-1),
    HALF_LEFT(-0.5),
    STRAIGHT(0),
    HALF_RIGHT(0.5),
    FULL_RIGHT(1);

    private double value;

    public double getValue() {
        return this.value;
    }

    private STEERING_STATE(double value) {
        this.value = value;
    }
}
