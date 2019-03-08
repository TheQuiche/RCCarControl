package domain;

enum MotorType {
    ENGINE(5, 12),
    STEERING(6, 13);
    
    private final int dirPin, powerPin;
    
    int getDirPin() {
        return dirPin;
    }
    
    int getPowerPin() {
        return powerPin;
    }
    
    MotorType(int dirPin, int powerPin) {
        this.dirPin = dirPin;
        this.powerPin = powerPin;
    }
}
