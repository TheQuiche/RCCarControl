package main;

import com.studiohartman.jamepad.ControllerManager;
import com.studiohartman.jamepad.ControllerState;
import domain.DomainController;
import java.util.InputMismatchException;
import java.util.Scanner;

class InputProvider {

    private boolean end = false;
    private static final ControllerManager CONTROLLERS = new ControllerManager();
    private ControllerState state;
    private final DomainController dc;
    private final Scanner input;
    private int userInput;

    InputProvider() {
        dc = new DomainController();
        input = new Scanner(System.in);
        CONTROLLERS.initSDLGamepad();
        state = CONTROLLERS.getState(0);
    }

    void run() {
        selectInputType();

        switch (userInput) {
            case 1:
                selectIP();
                scanControllerInput();
                break;

            case 2:
                selectIP();
                scanKeyboardInput();
                break;

            case 99:
                System.exit(0);

            default:
                System.out.println("Something went wrong while determining the input type...");
                System.exit(1);
        }
    }

    private void selectInputType() {
        System.out.println("Please select an input type:");
        System.out.println("  1 - XBOX 360/One controller");
        System.out.println("  2 - Keyboard");
        System.out.println("  99 - Exit the program");
        System.out.println("");
        System.out.print("Enter your choice here: ");

        try {
            userInput = input.nextInt();
            input.nextLine();   // Clear the buffer (contains <Enter>)

        } catch (InputMismatchException ex) {
            userInput = -1; // Set the entered value as a non-valid one so the program asks again
        }

        while (userInput != 1 && userInput != 2 && userInput != 99) {
            System.out.println("");
            System.out.println("Incorrect input type! Try again:");
            System.out.println("  1 - XBOX 360/One controller");
            System.out.println("  2 - Keyboard");
            System.out.println("  99 - Exit the program");
            System.out.println("");
            System.out.print("Enter your choice here: ");

            try {
                userInput = input.nextInt();
                input.nextLine();

            } catch (InputMismatchException ex) {
                userInput = -1; // Set the entered value as a non-valid one so the program asks again
            }
        }
    }

    private void selectIP() {
        System.out.print("Enter the RC Car IP address: ");

        while (!dc.setServerIP(input.nextLine())) {
            System.out.println();
            System.out.print("Enter the RC Car server IP address: ");
        }

        System.out.println("Succesfully connected to the server!");
        System.out.println();
    }

    private void scanControllerInput() {
        if (!state.isConnected) { // Check if a controller is connected
            System.out.println("No controller found, connect a controller and restart this application.");
            System.exit(1);
        }

        System.out.println("Controller keymap:");   // Print controller keymapping
        System.out.println("  LT (Left Trigger):    Reverse/Brake");
        System.out.println("  RT (Right Trigger):   Accelerate");
        System.out.println("  LS (Left Joystick):   Steer left/right");
        System.out.println("  START:                Exit the program");
        System.out.println("");
        System.out.println("The program is ready to receive your input!");
        System.out.println("");

        while (!end) {
            state = CONTROLLERS.getState(0); // Get current controller state

            if (state.start) { // Exit the program when the start button is pressed
                System.out.println("Start button pressed, exiting now...");
                end = true;
            }

            // Send the current input values to the DomainController
            dc.updateCurrentSteeringValue(state.leftStickX);
            dc.updateCurrentEngineValue(interpretTriggers(state.leftTrigger, state.rightTrigger));
        }
    }

    private void scanKeyboardInput() {
        System.out.println("keyboard keymap:");   // Print controller keymapping
        System.out.println("  W:        Accelerate");
        System.out.println("  S:        Reverse/Brake");
        System.out.println("  A:        Steer left");
        System.out.println("  D:        Steer right");
        System.out.println("  Shift:    Full engine speed (forward & backward)");
        System.out.println("  Esc:      Exit the program");
        System.out.println("");
        System.out.println("The program is NOT ready to receive your input!");
        System.out.println("");

        // To-Do: Add keyboard as an input option
        System.exit(1);
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
