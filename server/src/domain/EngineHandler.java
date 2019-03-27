package domain;

import static domain.MotorType.ENGINE;
import static domain.EngineState.*;
import java.io.IOException;
import java.sql.Timestamp;

class EngineHandler implements Runnable {

    private final DomainController dc;
    private boolean isChangingThrottle = false; // This is set to true if you quickly floor the throttle (the program will then slowly accelerate, this is needed for not powerfull engines)
    private EngineState currentState, newState = IDLE;
    private long prevChangeTimestamp = new Timestamp(System.currentTimeMillis()).getTime(), currentTimestamp;  // Used to detect fast throttle changes
    private final static long SLEEPBETWEENTHROTTLECHANGES = 1500; // This is needed for weak engines that can't handle quick throttle changes

    EngineHandler(DomainController dc) {
        this.dc = dc;
        setupGPIO();
    }

    boolean isChangingThrottle() {
        return isChangingThrottle;
    }

    private void setupGPIO() {
        try {
            Runtime.getRuntime().exec("setup_RCCarGPIO.sh");
            System.out.println("GPIO setup!");

        } catch (IOException ex) {
            System.out.println("Something went wrong while running the GPIO setup!");
            System.exit(1);
        }
    }

    @Override
    public void run() {
        while (true) {
            newState = EngineState.getByName(dc.get(ENGINE));   // Get the updated engine state from the ABQ

            if (checkAllowedSkip()) {
                continue;   // currentState updated, no need to loop through the next if's
            }

            if (checkThrottleSkip()) {
                continue;
            }

            if (checkFastChange()) {
                continue;
            }

            handleOutput(currentState = newState);  // When we have a change that isn't special
            prevChangeTimestamp = new Timestamp(System.currentTimeMillis()).getTime();  // Update the previous change timestamp

        }
    }

    private boolean checkAllowedSkip() {    // returns true if the throttle position skip is allowed and executed
        if (newState == IDLE) { // Going to idle can happen immediately
            handleOutput(currentState = newState);
            prevChangeTimestamp = new Timestamp(System.currentTimeMillis()).getTime();  // Update the previous change timestamp

            return true;   // currentState updated, no need to loop through the next if's
        }

        if (currentState.getValue() < 0) {  // We need to know this to decide if we're decelerating or accelerating
            if (currentState.getValue() < newState.getValue() && newState.getValue() <= 1) {   // Here we are decelerating, no slowly changing throttle needed
                handleOutput(currentState = newState);  // Update the currentEngineState and write changes to GPIO
                prevChangeTimestamp = new Timestamp(System.currentTimeMillis()).getTime();  // Update the previous change timestamp

                return true;   // currentState updated, no need to loop through the next if's
            }

        } else if (currentState.getValue() > newState.getValue() && newState.getValue() <= -1) {   // Here we are decelerating, no slowly changing throttle needed
            handleOutput(currentState = newState);
            prevChangeTimestamp = new Timestamp(System.currentTimeMillis()).getTime();  // Update the previous change timestamp

            return true;
        }

        return false;   // Skip is not allowed so the other checks need to be performed
    }

    private boolean checkThrottleSkip() {
        if (Math.abs(currentState.getValue() - newState.getValue()) > 1) {  // If the absolute value of the subtraction is larger than 1, you skipped a throttle position (see EngineState enum values)
            isChangingThrottle = true;  // We will now start slowly changing the throttle

            System.out.printf("Slowly going from '%s' to '%s'%n", currentState.name(), newState.name());

            while (currentState != newState) { // As long as we aren't where the user wants it, slowly increase/decrease the throtlle
                if (currentState.getValue() < newState.getValue()) {   // We now know we are accelerating
                    handleOutput(currentState = EngineState.getByValue(currentState.getValue() + 1));
                    prevChangeTimestamp = new Timestamp(System.currentTimeMillis()).getTime();  // Update the previous change timestamp

                } else {    // We now know we are decelerating
                    handleOutput(currentState = EngineState.getByValue(currentState.getValue() - 1));
                    prevChangeTimestamp = new Timestamp(System.currentTimeMillis()).getTime();  // Update the previous change timestamp
                }

                try {
                    Thread.sleep(SLEEPBETWEENTHROTTLECHANGES);

                } catch (InterruptedException ex) { // This happens when the user gives other engine input while we are slowly changing it
                    dc.clearEngineBuffer(); // We have to clear the engineBuffer so the next value will be the newly inserted user input
                }
            }

            isChangingThrottle = false; // We are done changing the throttle
            return true;
        } else {
            return false;
        }
    }

    private boolean checkFastChange() {
        currentTimestamp = new Timestamp(System.currentTimeMillis()).getTime();

        if (currentTimestamp + SLEEPBETWEENTHROTTLECHANGES < prevChangeTimestamp) { // true when the last change was less than 1.5 seconds ago
            isChangingThrottle = true;

            while (currentTimestamp > prevChangeTimestamp + SLEEPBETWEENTHROTTLECHANGES) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) { // This happens when the user gives other engine input while we are waiting to change it
                    dc.clearEngineBuffer(); // We have to clear the engineBuffer so the next value will be the newly inserted user input
                }
            }

            isChangingThrottle = false;
            return true;
        } else {
            return false;
        }
    }

    private void handleOutput(EngineState state) {
        try {
            Runtime.getRuntime().exec(String.format("gpio -g write 5 %d && gpio -g pwm 12 %d", state.getDirection(), state.getPower()));
            System.out.println("Written: " + state.name());

        } catch (IOException ex) {
            System.out.println("Something went wrong while changing the GPIO values!");
        }
    }
}
