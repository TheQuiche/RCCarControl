package domain;

public class DomainController {
    private final InputHandler ih;
    
    public DomainController() {
        ih = new InputHandler();
    }

    public void updateCurrentSteeringValue(float steeringInputValue) {
        ih.handle("S" + steeringInputValue);    // Let the InputHandler do it's thing with the value
    }

    public void updateCurrentEngineValue(float engineInputValue) {
        ih.handle("E" + engineInputValue);    // Let the InputHandler do it's thing with the value
    }
}
