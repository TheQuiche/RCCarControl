package domain;

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
            System.out.println("Something went wrong while creating the websocket!");
            System.exit(1);
        }
    }

    void handle() {
        while (true) {
            try {
                socket.receive(receivedPacket); // Wait for input to fill the array
                dc.updateValue(new String(receivedPacket.getData(), receivedPacket.getOffset(), receivedPacket.getLength())); // Only use the new received bytes
                System.out.println("received " + new String(receivedPacket.getData(), receivedPacket.getOffset(), receivedPacket.getLength()));
                
            } catch (IOException ex) {
                System.out.println("Something went wrong while receiving a packet!");
            }
        }
    }
}
