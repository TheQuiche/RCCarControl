package domain;

enum ENGINE_STATE {     // States starting with 0 are for the engine
    FULL_REVERSE("00"),
    HALF_REVERSE("01"),
    QUARTER_REVERSE("02"),
    COASTING("03"),
    QUARTER_THROTTLE("04"),
    HALF_THROTTLE("05"),
    FULL_THROTTLE("06");

    private String value;

    String getValue() {
        return this.value;
    }

    private ENGINE_STATE(String value) {
        this.value = value;
    }
}
