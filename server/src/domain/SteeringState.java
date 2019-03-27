package domain;

enum SteeringState {
    FULL_LEFT(1, 480),
    HALF_LEFT(1, 200),
    STRAIGHT(0, 0),
    HALF_RIGHT(0, 200),
    FULL_RIGHT(0, 480);

    private final int direction, power;

    SteeringState(int direction, int power) {
        this.direction = direction;
        this.power = power;
    }

    int getDirection() {
        return direction;
    }

    int getPower() {
        return power;
    }

    static SteeringState getByName(String name) {
        switch (name) {
            case "FULL_LEFT":
                return FULL_LEFT;

            case "HALF_LEFT":
                return HALF_LEFT;

            case "STRAIGHT":
                return STRAIGHT;

            case "HALF_RIGHT":
                return HALF_RIGHT;

            case "FULL_RIGHT":
                return FULL_RIGHT;

            default:
                System.out.println("We received a wierd value in getByValue (EngineState)");
                return null;
        }
    }
}
