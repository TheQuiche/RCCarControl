package domain;

enum SteeringState {
    FULL_LEFT(480),
    HALF_LEFT(200),
    STRAIGHT(0),
    HALF_RIGHT(200),
    FULL_RIGHT(480);

    private final int value;

    SteeringState(int value) {
        this.value = value;
    }

    int getValue() {
        return value;
    }
}
