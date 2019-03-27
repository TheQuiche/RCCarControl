package domain;

import static domain.MotorType.*;

public class DomainController {

    private final InputHandler ih;
    private final ConnectionHandler ch;

    public DomainController() {
        ch = new ConnectionHandler();
        ih = new InputHandler(this);
    }

    // Methods for InputProvider
    public boolean setServerIP(String IP) {
        return ch.setServerIP(IP);
    }

    public void updateInput(float steeringInputValue, float engineInputValue) {
        ih.handle(ENGINE, engineInputValue);    // Let the InputHandler do it's thing with the value
        ih.handle(STEERING, steeringInputValue);    // Let the InputHandler do it's thing with the value
    }

    // Methods for InputHandler
    void sendData(byte[] data) {
        ch.send(data);
    }
}
