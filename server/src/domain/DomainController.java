package domain;

public class DomainController {

    private ConnectionHandler ch;
    private MotorHandler mh;
    
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
