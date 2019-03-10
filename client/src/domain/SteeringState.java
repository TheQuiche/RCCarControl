package domain;

enum SteeringState {
    FULL_LEFT(-1),
    HALF_LEFT(-0.5),
    STRAIGHT(0),
    HALF_RIGHT(0.5),
    FULL_RIGHT(1);

    private final double value;

    SteeringState(double value) {
        this.value = value;
    }
    
    double getValue() {
        return value;
    }
}
