package domain;

import static domain.MotorType.*;
import static domain.SteeringState.*;
import static domain.EngineState.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

class MotorHandler {

    MotorHandler() {
        setupGPIO();
    }

    void handle(String data) {
        switch (data) {
            // Steering states
            case "FULL_LEFT":
                handleOutput(STEERING, 1, FULL_LEFT.getValue());
                break;

            case "HALF_LEFT":
                handleOutput(STEERING, 1, HALF_LEFT.getValue());
                break;

            case "STRAIGHT":
                handleOutput(STEERING, 0, STRAIGHT.getValue());
                break;

            case "HALF_RIGHT":
                handleOutput(STEERING, 0, HALF_RIGHT.getValue());
                break;

            case "FULL_RIGHT":
                handleOutput(STEERING, 0, FULL_RIGHT.getValue());
                break;

            // Engine states
            case "FULL_REVERSE":
                handleOutput(ENGINE, 1, FULL_REVERSE.getValue());
                break;

            case "HALF_REVERSE":
                handleOutput(ENGINE, 1, HALF_REVERSE.getValue());
                break;

            case "QUARTER_REVERSE":
                handleOutput(ENGINE, 1, QUARTER_REVERSE.getValue());
                break;

            case "IDLE":
                handleOutput(ENGINE, 0, IDLE.getValue());
                break;

            case "QUARTER_FORWARD":
                handleOutput(ENGINE, 0, QUARTER_FORWARD.getValue());
                break;

            case "HALF_FORWARD":
                handleOutput(ENGINE, 0, HALF_FORWARD.getValue());
                break;

            case "FULL_FORWARD":
                handleOutput(ENGINE, 0, FULL_FORWARD.getValue());
        }

        System.out.println("received " + data);
    }

    private void setupGPIO() {
        try {
            Runtime.getRuntime().exec("setup_RCCarGPIO.sh");
        } catch (IOException ex) {
            Logger.getLogger(MotorHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("GPIO setup!");
    }

    private void handleOutput(MotorType motorType, int direction, int power) {
        try {
            Runtime.getRuntime().exec(String.format("gpio -g write %d %d", motorType.getDirPin(), direction));
            Runtime.getRuntime().exec(String.format("gpio -g pwm %d %d", motorType.getPowerPin(), power));
        } catch (IOException ex) {
            Logger.getLogger(MotorHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
