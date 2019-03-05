package domain;

enum STEERING_STATE {   // States starting with 1 are for the steering
    FULL_LEFT("10"),
    HALF_LEFT("11"),
    STRAIGHT("12"),
    HALF_RIGHT("13"),
    FULL_RIGHT("14");

    private String value;

    String getValue() {
        return this.value;
    }

    private STEERING_STATE(String value) {
        this.value = value;
    }
}
