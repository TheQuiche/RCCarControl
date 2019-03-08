package domain;

import static domain.EngineState.*;
import static domain.SteeringState.*;

class InputHandler {

    private DomainController dc;
    private EngineState prev_engineState, new_engineState;
    private SteeringState prev_steeringState, new_steeringState;

    InputHandler(DomainController dc) {
        this.dc = dc;
        prev_engineState = IDLE;
        prev_steeringState = STRAIGHT;
    }

    void handle(String input) {
        switch (input.charAt(0)) { // Interpret what type of input it is
            case 'S':   // Input is a steering value
                new_steeringState = convertSteering(Float.parseFloat(input.substring(1))); // Everything after the first char is the input value
                if (new_steeringState != prev_steeringState) {
                    dc.sendData(new_steeringState.name().getBytes()); // Let the ConnectionHandler send the data because it's a changed value

                    prev_steeringState = new_steeringState;
                }
                break;

            case 'E':   // Input is an engine value
                new_engineState = convertThrottle(Float.parseFloat(input.substring(1))); // Everything after the first char is the input value
                if (new_engineState != prev_engineState) {
                    dc.sendData(new_engineState.name().getBytes()); // Let the ConnectionHandler send the data because it's a changed value

                    prev_engineState = new_engineState;
                }
                break;
        }
    }

    private SteeringState convertSteering(float steeringInputValue) {
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

    private EngineState convertThrottle(float engineInputValue) {
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
            return IDLE;
        }
        if (engineInputValue < 0.3) {
            return QUARTER_FORWARD;
        }
        if (engineInputValue < 0.6) {
            return HALF_FORWARD;
        } else {
            return FULL_FORWARD;
        }
    }
}
