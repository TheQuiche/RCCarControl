package domain;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

class ConnectionHandler {

    private DatagramSocket socket = null;
    private final static int PORT = 1234;
    private static InetAddress IPADDRESS;

    ConnectionHandler() {
        try {
            socket = new DatagramSocket(PORT);

        } catch (SocketException ex) {
            System.out.println("Something went wrong while creating the DatagramSocket!");
            System.exit(1);
        }
    }

    boolean setServerIP(String serverIP) {
        try {
            IPADDRESS = InetAddress.getByName(serverIP);

            return testServerConnection();

        } catch (UnknownHostException ex) {
            return false;   // Return false if the IP is invalid
        }
    }

    private boolean testServerConnection() { // Check if the server is correctly setup
        DatagramPacket sendPacket = new DatagramPacket("REQUEST".getBytes(), 7, IPADDRESS, PORT);

        try {
            socket.send(sendPacket);    // Send a request to the server (expects ACK)

        } catch (IOException ex) {
            System.out.println("Something went wrong while connecting to the server!");
            return false;
        }

        return checkServerResponse();
    }

    private boolean checkServerResponse() {
        boolean received = false;
        byte[] receivedData;
        final DatagramPacket receivedPacket;
        receivedData = new byte[3];
        receivedPacket = new DatagramPacket(receivedData, receivedData.length);

        while (!received) {
            try {
                socket.setSoTimeout(1000); // Set time to wait for ACK packet
                socket.receive(receivedPacket); // Wait for input to fill the array
                return new String(receivedPacket.getData()).equals("ACK"); // Check if the response is as expected (= ACK)

            } catch (IOException ex) {
                System.out.println("Something went wrong while connecting to the server!");
                return false;
            }
        }

        return false;
    }

    void send(byte[] data) {
        System.out.printf("Sending: '%s'%n", new String(data));
        DatagramPacket sendPacket = new DatagramPacket(data, data.length, IPADDRESS, PORT);

        try {
            socket.send(sendPacket);

        } catch (IOException ex) {
            System.out.println("Something went wrong while sending the data (check the entered IP-address!)!");
        }
    }
}
