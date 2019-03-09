package domain;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

class ConnectionHandler {

  private final DomainController dc;
  private DatagramSocket socket;
  private final int PORT;
  private final byte[] receivedData;
  private final DatagramPacket receivedPacket;

  ConnectionHandler(DomainController dc) {
    this.dc = dc;
    PORT = 1234;
    receivedData = new byte[15];
    receivedPacket = new DatagramPacket(receivedData, receivedData.length);

    try {
      socket = new DatagramSocket(PORT);
    } catch (SocketException ex) {
      Logger.getLogger(ConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
      System.out.println("Something went wrong while creating the websocket!");
      System.exit(1);
    }
  }

  void handle() {
    while (true) {
      try {
          Arrays.fill(null, receivedData); // Empty the array
        socket.receive(receivedPacket); // Wait for input to fill it
      } catch (IOException ex) {
        Logger.getLogger(ConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
      }

      dc.updateValue(new String(receivedData));
    }
  }
}
