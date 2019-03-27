package domain;

import static domain.MotorType.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

class ConnectionHandler {

    private final DomainController dc;
    private DatagramSocket socket;
    private final int PORT;
    private byte[] receivedData;
    private final DatagramPacket receivedPacket;

    ConnectionHandler(DomainController dc) {
        this.dc = dc;
        PORT = 1234;
        receivedData = new byte[15];
        receivedPacket = new DatagramPacket(receivedData, receivedData.length);

        try {
            socket = new DatagramSocket(PORT);

        } catch (SocketException ex) {
            System.out.println("Something went wrong while creating the DatagramSocket!");
            System.exit(1);
        }
    }

    public void run() {
        String receivedDataString;
        System.out.println("Waiting for a client to connect...");

        while (true) {
            try {
                socket.receive(receivedPacket); // Wait for input to fill the array
                receivedDataString = new String(receivedPacket.getData(), receivedPacket.getOffset(), receivedPacket.getLength()); // Only use the new received bytes

                if (receivedDataString.equals("REQUEST")) {
                    System.out.println("New client is connected!");
                    sendACK();

                } else {
                    System.out.printf("Received: '%s'%n", receivedDataString);

                    if (receivedDataString.endsWith("_REVERSE") || receivedDataString.equals("IDLE") || receivedDataString.endsWith("_FORWARD")) { // Check if the input is for the EngineHandler
                        if (dc.isChangingThrottle()) {
                            dc.interruptEngineHandlerThread(); // If we interrupt the slowly throttle changing thread, it will pick the next value from the buffer
                        }
                        
                        dc.set(ENGINE, receivedDataString);
                        continue; // skip next if
                    }

                    if (receivedDataString.endsWith("_LEFT") || receivedDataString.equals("STRAIGHT") || receivedDataString.endsWith("_RIGHT")) { // Check if the input is for SteeringHandler
                        dc.set(STEERING, receivedDataString); // Error lies behind this
                        continue; // skip next if's if i would add any
                    }

                    System.out.println("We received a wierd packet!");
                }

            } catch (IOException ex) {
                System.out.println("Something went wrong while receiving a packet!");
            }
        }
    }

    private void sendACK() {
        try {
            socket.send(new DatagramPacket("ACK".getBytes(), 3, receivedPacket.getAddress(), PORT)); // Send ACK to the client that sent the REQUEST packet
        } catch (IOException ex) {
            System.out.println("Something went wrong while sending an acknowledgement to the client!");
        }
    }
}
