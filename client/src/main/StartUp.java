package main;

import com.studiohartman.jamepad.ControllerManager;
import com.studiohartman.jamepad.ControllerState;
import domain.ENGINE_STATE;
import static domain.ENGINE_STATE.*;
import domain.STEERING_STATE;
import static domain.STEERING_STATE.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StartUp {

    public static void main(String[] args) throws IOException {
        DatagramSocket socket = null;
        byte[] sendData = new byte[6];
        final InetAddress IPAddress = InetAddress.getByName("192.168.0.20");
        final int PORT = 1234;
        boolean end = false;
        ControllerManager controllers = new ControllerManager();
        ControllerState state;
        ENGINE_STATE prev_engineState = COASTING, new_engineState = COASTING;
        STEERING_STATE prev_steeringState = STRAIGHT, new_steeringState = STRAIGHT;
        try {
            socket = new DatagramSocket(PORT);
        } catch (SocketException ex) {
            Logger.getLogger(StartUp.class.getName()).log(Level.SEVERE, null, ex);
        }

        controllers.initSDLGamepad();
        state = controllers.getState(0);

        if (!state.isConnected) {
            System.out.println("No controller found, connect a controller and restart this application.");
            System.exit(1);
        }
        while (!end) {
            state = controllers.getState(0);

            if (state.start) {
                System.out.println("Start button pressed, exitting now...");
                end = true;
            }

            // Check steering
            new_steeringState = convertSteering(state.leftStickX);
            if (new_steeringState != prev_steeringState) {
                sendData = String.format("s%f", new_steeringState.getValue()).getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, PORT);
                socket.send(sendPacket);
                System.out.println("Sent " + new_steeringState);

                prev_steeringState = new_steeringState;
            }

            // Check throttle
            new_engineState = convertThrottle(state.leftTrigger, state.rightTrigger);
            if (new_engineState != prev_engineState) {
                sendData = String.format("e%f", new_engineState.getValue()).getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, PORT);
                socket.send(sendPacket);
                System.out.println("Sent " + new_engineState);

                prev_engineState = new_engineState;
            }
        }
    }

    private static STEERING_STATE convertSteering(float leftStickX) {
        if (leftStickX < -0.5) {
            return FULL_LEFT;
        }
        if (leftStickX < -0.1) {
            return HALF_LEFT;
        }                           // deadzone [-0.1, 0.1]
        if (leftStickX < 0.1) {
            return STRAIGHT;
        }
        if (leftStickX < 0.6) {
            return HALF_RIGHT;
        } else {
            return FULL_RIGHT;
        }
    }

    private static float interpretTriggers(float leftTrigger, float rightTrigger) {
        if (leftTrigger > 0.1 && rightTrigger > 0.1) {  // deadzone [0, 0.1]
            if (leftTrigger > rightTrigger) {
                return -leftTrigger + rightTrigger; // return negative left + right because the returned value must be negative (= reverse)
            } else {
                return rightTrigger - leftTrigger; // return right - left because the returned value must be positive (= forward)
            }
        }
        if (leftTrigger > 0.1) {
            return -leftTrigger;
        }
        if (rightTrigger > 0.1) {
            return rightTrigger;
        }
        return 0;
    }

    private static ENGINE_STATE convertThrottle(float leftTrigger, float rightTrigger) {
        float newValue = interpretTriggers(leftTrigger, rightTrigger);

        if (newValue < -0.6) {
            return FULL_REVERSE;
        }
        if (newValue < -0.3) {
            return HALF_REVERSE;
        }
        if (newValue < 0) {
            return QUARTER_REVERSE;
        }
        if (newValue == 0) {
            return COASTING;
        }
        if (newValue < 0.3) {
            return QUARTER_THROTTLE;
        }
        if (newValue < 0.6) {
            return HALF_THROTTLE;
        } else {
            return FULL_THROTTLE;
        }
    }
}
