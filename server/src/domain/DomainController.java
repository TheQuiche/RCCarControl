package domain;

public class DomainController {

    private final ConnectionHandler ch;
    private final MotorHandler mh;
    
    public DomainController() {
        mh = new MotorHandler();
        ch = new ConnectionHandler(this);
    }

    public void run() {
        ch.handle();
    }
    
    void updateValue(String data) {
        mh.handle(data);
    }
}
