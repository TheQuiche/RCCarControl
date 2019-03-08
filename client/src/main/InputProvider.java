package main;

import com.studiohartman.jamepad.ControllerManager;
import com.studiohartman.jamepad.ControllerState;
import domain.DomainController;

class InputProvider {

    private boolean end = false;
    private static final ControllerManager CONTROLLERS = new ControllerManager();
    private ControllerState state;
    private final DomainController dc;

    InputProvider() {
        dc = new DomainController();
        CONTROLLERS.initSDLGamepad();
        state = CONTROLLERS.getState(0);
    }

    void run() {
        if (!state.isConnected) { // Check if a controller is connected
            System.out.println("No controller found, connect a controller and restart this application.");
            System.exit(1);
        }

        while (!end) {
            state = CONTROLLERS.getState(0); // Get current controller state

            if (state.start) { // Exit the program when the start button is pressed
                System.out.println("Start button pressed, exitting now...");
                end = true;
            }

            // Send the current input values to the DomainController
            dc.updateCurrentSteeringValue(state.leftStickX);
            dc.updateCurrentEngineValue(interpretTriggers(state.leftTrigger, state.rightTrigger));
        }
    }

    private float interpretTriggers(float leftTrigger, float rightTrigger) {
        if (leftTrigger > 0.1 && rightTrigger > 0.1) {  // deadzone [0, 0.1]
            if (leftTrigger > rightTrigger) {
                return -leftTrigger + rightTrigger; // return negative left + right because the returned value must be negative (= reverse)
            } else {
                return rightTrigger - leftTrigger; // return right - left because the returned value must be positive (= forward)
            }
        }
        if (leftTrigger > 0.1) { // deadzone [0, 0.1]
            return -leftTrigger;
        }
        if (rightTrigger > 0.1) { // deadzone [0, 0.1]
            return rightTrigger;
        }
        return 0;
    }
}
