package domain;

enum EngineState {

    FULL_REVERSE(-1),
    HALF_REVERSE(-0.6),
    QUARTER_REVERSE(-0.3),
    IDLE(0),
    QUARTER_FORWARD(0.3),
    HALF_FORWARD(0.6),
    FULL_FORWARD(1);

    private final double value;

    EngineState(double value) {
        this.value = value;
    }

    double getValue() {
        return value;
    }
}
