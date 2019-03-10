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
            return true;    // Return true if the IP is valid
            
        } catch (UnknownHostException ex) {
            System.out.println("Something went wrong while creating the InetAddress!");
            return false;   // Return false if the IP is invalid
        }
    }

    void send(byte[] data) {
        System.out.println("sending " + new String(data));
        DatagramPacket sendPacket = new DatagramPacket(data, data.length, IPADDRESS, PORT);

        try {
            socket.send(sendPacket);
            
        } catch (IOException ex) {
            System.out.println("Something went wrong while sending the data!");
        }
    }
}
