package domain;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

class ConnectionManager implements Runnable {

  private final DomeinController dc;
  private DatagramSocket socket = null;
  private final int PORT;
  private final byte[] receivedData;
  private final DatagramPacket receivedPacket;

  ConnectionManager(DomeinController dc) {
    this.dc = dc;
    PORT = 1234;
    receivedData = new byte[6];
    receivedPacket = new DatagramPacket(receivedData, receivedData.length);

    try {
      socket = new DatagramSocket(PORT);
    } catch (SocketException ex) {
      Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  @Override
  public void run() {
    while (true) {
      try {
        socket.receive(receivedPacket);
      } catch (IOException ex) {
        Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, null, ex);
      }
      
      dc.set(new String(receivedData));
    }
  }
}
