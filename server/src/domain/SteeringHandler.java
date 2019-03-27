package domain;

import static domain.MotorType.STEERING;
import java.io.IOException;

class SteeringHandler implements Runnable {

    private final DomainController dc;

    SteeringHandler(DomainController dc) {
        this.dc = dc;
    }

    @Override
    public void run() {
        while (true) {
            handleOutput(SteeringState.getByName(dc.get(STEERING)));
        }
    }

    private void handleOutput(SteeringState state) {
        try {
            Runtime.getRuntime().exec(String.format("gpio -g write 6 %d && gpio -g pwm 13 %d", state.getDirection(), state.getPower()));
            System.out.println("Written: " + state.name());

        } catch (IOException ex) {
            System.out.println("Something went wrong while changing the GPIO values!");
        }
    }
}
