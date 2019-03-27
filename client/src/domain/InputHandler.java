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

    void handle(MotorType motorType, float inputValue) {
        switch (motorType) {
            case ENGINE:
                new_engineState = convertThrottle(inputValue); // Everything after the first char is the input value
                if (new_engineState != prev_engineState) {
                    dc.sendData(new_engineState.name().getBytes()); // Let the ConnectionHandler send the data because it's a changed value

                    prev_engineState = new_engineState;
                }
                break;

            case STEERING:
                new_steeringState = convertSteering(inputValue); // Everything after the first char is the input value
                if (new_steeringState != prev_steeringState) {
                    dc.sendData(new_steeringState.name().getBytes()); // Let the ConnectionHandler send the data because it's a changed value

                    prev_steeringState = new_steeringState;
                }
                break;
        }
    }

    private EngineState convertThrottle(float engineInputValue) {
        if (engineInputValue < HALF_REVERSE.getValue()) {
            return FULL_REVERSE;
        }
        if (engineInputValue < QUARTER_REVERSE.getValue()) {
            return HALF_REVERSE;
        }
        if (engineInputValue < IDLE.getValue() - 0.1) { // deadzone [-0.1, 0.1]
            return QUARTER_REVERSE;
        }
        if (engineInputValue < IDLE.getValue() + 0.1) { // deadzone [-0.1, 0.1]
            return IDLE;
        }
        if (engineInputValue < QUARTER_FORWARD.getValue()) {
            return QUARTER_FORWARD;
        }
        if (engineInputValue < HALF_FORWARD.getValue()) {
            return HALF_FORWARD;
        }
        if (engineInputValue <= FULL_FORWARD.getValue()) {
            return FULL_FORWARD;
        }

        return IDLE;    // In case something went wrong we'll just pick idle
    }

    private SteeringState convertSteering(float steeringInputValue) {
        if (steeringInputValue < HALF_LEFT.getValue()) {
            return FULL_LEFT;
        }
        if (steeringInputValue < STRAIGHT.getValue() - 0.1) { // deadzone [-0.1, 0.1]
            return HALF_LEFT;
        }
        if (steeringInputValue < STRAIGHT.getValue() + 0.1) { // deadzone [-0.1, 0.1]
            return STRAIGHT;
        }
        if (steeringInputValue < HALF_RIGHT.getValue()) {
            return HALF_RIGHT;
        }
        if (steeringInputValue <= FULL_RIGHT.getValue()) {
            return FULL_RIGHT;
        }

        return STRAIGHT;    // In case something went wrong we'll just pick straight
    }
}
