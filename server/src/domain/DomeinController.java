package domain;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DomeinController {

  private final ArrayBlockingQueue<String> buffer;
  private final Thread conn, engine;

  public DomeinController() {
    buffer = new ArrayBlockingQueue<>(10);
    conn = new Thread(new ConnectionManager(this));
    engine = new Thread(new MotorManager(this));

    conn.start();
    engine.start();
  }

  public void set(String value) {
    try {
      buffer.put(value);
    } catch (InterruptedException ex) {
      Logger.getLogger(DomeinController.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public String get() {
    try {
      return buffer.take();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      return null;
    }
  }
}