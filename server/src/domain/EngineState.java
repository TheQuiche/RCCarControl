package domain;

enum EngineState {

    FULL_REVERSE(1, 480, -3),
    HALF_REVERSE(1, 300, -2),
    QUARTER_REVERSE(1, 150, -1),
    IDLE(0, 0, 0),
    QUARTER_FORWARD(0, 150, 1),
    HALF_FORWARD(0, 300, 2),
    FULL_FORWARD(0, 480, 3);

    private final int direction, power, value;

    EngineState(int direction, int power, int value) {
        this.direction = direction;
        this.power = power;
        this.value = value;
    }

    int getDirection() {
        return direction;
    }

    int getPower() {
        return power;
    }

    int getValue() {
        return value;
    }

    static EngineState getByName(String name) {
        switch (name) {
            case "FULL_REVERSE":
                return FULL_REVERSE;

            case "HALF_REVERSE":
                return HALF_REVERSE;

            case "QUARTER_REVERSE":
                return QUARTER_REVERSE;

            case "IDLE":
                return IDLE;

            case "QUARTER_FORWARD":
                return QUARTER_FORWARD;

            case "HALF_FORWARD":
                return HALF_FORWARD;

            case "FULL_FORWARD":
                return FULL_FORWARD;

            default:
                System.out.println("We received a wierd value in getByValue (EngineState)");
                return null;
        }
    }

    static EngineState getByValue(int value) {
        switch (value) {
            case -3:
                return FULL_REVERSE;

            case -2:
                return HALF_REVERSE;

            case -1:
                return QUARTER_REVERSE;

            case 0:
                return IDLE;

            case 1:
                return QUARTER_FORWARD;

            case 2:
                return HALF_FORWARD;

            case 3:
                return FULL_FORWARD;

            default:
                System.out.println("We received a wierd value in getByValue (EngineState)");
                return null;
        }
    }
}
