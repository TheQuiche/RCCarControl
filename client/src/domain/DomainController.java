package domain;

public class DomainController {

    private final InputHandler ih;
    private final ConnectionHandler ch;

    public DomainController() {
        ch = new ConnectionHandler();
        ih = new InputHandler(this);
    }

    // Methods for InputProvider
    public void updateCurrentSteeringValue(float steeringInputValue) {
        ih.handle("S" + steeringInputValue);    // Let the InputHandler do it's thing with the value
    }

    public void updateCurrentEngineValue(float engineInputValue) {
        ih.handle("E" + engineInputValue);    // Let the InputHandler do it's thing with the value
    }

    // Methods for InputHandler
    void sendData(byte[] data) {
        ch.send(data);
    }
}
