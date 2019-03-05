package domain;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DomainController {

  private final ArrayBlockingQueue<String> buffer;

  public DomainController() {
    buffer = new ArrayBlockingQueue<>(10);
    new ConnectionManager(this).run();
    new MotorManager(this).run();
  }

  public void set(String value) {
    try {
      buffer.put(value);
    } catch (InterruptedException ex) {
      Logger.getLogger(DomainController.class.getName()).log(Level.SEVERE, null, ex);
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