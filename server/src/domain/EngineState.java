package domain;

enum EngineState {

    FULL_REVERSE(480),
    HALF_REVERSE(300),
    QUARTER_REVERSE(150),
    IDLE(0),
    QUARTER_FORWARD(150),
    HALF_FORWARD(300),
    FULL_FORWARD(480);

    private final int value;

    EngineState(int value) {
        this.value = value;
    }

    int getValue() {
        return value;
    }
}
