package domain;

import java.util.concurrent.ArrayBlockingQueue;

public class DomainController {

    private final ArrayBlockingQueue<String> engineBuffer, steeringBuffer;
    private final ConnectionHandler ch;
    private final EngineHandler eh;
    private final SteeringHandler sh;
    private final Thread ehThread;

    public DomainController() {
        engineBuffer = new ArrayBlockingQueue<>(10);
        steeringBuffer = new ArrayBlockingQueue<>(10);
        ch = new ConnectionHandler(this);
        ehThread = new Thread(eh = new EngineHandler(this));
        sh = new SteeringHandler(this);

    }

    public void run() {
        ch.run();
        ehThread.start();
        sh.run();
    }

    void set(MotorType motorType, String value) {
        try {
            switch (motorType) {
                case ENGINE:
                    engineBuffer.put(value);
                    break;

                case STEERING:
                    steeringBuffer.put(value);
                    break;
            }
        } catch (InterruptedException ex) {
            System.out.println("Something went wrong while putting stuff in the buffer!");
        }
    }

    String get(MotorType motorType) {
        try {
            switch (motorType) {
                case ENGINE:
                    return engineBuffer.take();

                case STEERING:
                    return steeringBuffer.take();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return "error";
    }

    boolean isChangingThrottle() {
        return eh.isChangingThrottle();
    }

    void interruptEngineHandlerThread() {
        ehThread.interrupt();
        engineBuffer.clear();
    }
}
