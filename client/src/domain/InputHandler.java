package domain;

import static domain.ENGINE_STATE.*;
import static domain.STEERING_STATE.*;

class InputHandler {

    private ConnectionHandler ch;
    private ENGINE_STATE prev_engineState, new_engineState;
    private STEERING_STATE prev_steeringState, new_steeringState;

    InputHandler() {
        prev_engineState = COASTING;
        prev_steeringState = STRAIGHT;
        ch = new ConnectionHandler();
    }

    void handle(String input) {
        switch (input.charAt(0)) { // Interpret what type of input it is
            case 'S':   // Input is a steering value
                new_steeringState = convertSteering(Float.parseFloat(input.substring(1))); // Everything after the first char is the input value
                if (new_steeringState != prev_steeringState) {
                    ch.handle(new_steeringState.getValue().getBytes()); // Let the ConnectionHandler send the data when it's a changed value

                    prev_steeringState = new_steeringState;
                }
                break;

            case 'E':   // Input is an engine value
                new_engineState = convertThrottle(Float.parseFloat(input.substring(1))); // Everything after the first char is the input value
                if (new_engineState != prev_engineState) {
                    ch.handle(new_engineState.getValue().getBytes()); // Let the ConnectionHandler send the data when it's a changed value

                    prev_engineState = new_engineState;
                }
                break;
        }
    }

    private static STEERING_STATE convertSteering(float steeringInputValue) {
        if (steeringInputValue < -0.5) {
            return FULL_LEFT;
        }
        if (steeringInputValue < -0.1) {
            return HALF_LEFT;
        }                           // deadzone [-0.1, 0.1]
        if (steeringInputValue < 0.1) {
            return STRAIGHT;
        }
        if (steeringInputValue < 0.6) {
            return HALF_RIGHT;
        } else {
            return FULL_RIGHT;
        }
    }

    private static ENGINE_STATE convertThrottle(float engineInputValue) {
        if (engineInputValue < -0.6) {
            return FULL_REVERSE;
        }
        if (engineInputValue < -0.3) {
            return HALF_REVERSE;
        }
        if (engineInputValue < 0) {
            return QUARTER_REVERSE;
        }
        if (engineInputValue == 0) {
            return COASTING;
        }
        if (engineInputValue < 0.3) {
            return QUARTER_THROTTLE;
        }
        if (engineInputValue < 0.6) {
            return HALF_THROTTLE;
        } else {
            return FULL_THROTTLE;
        }
    }
}
