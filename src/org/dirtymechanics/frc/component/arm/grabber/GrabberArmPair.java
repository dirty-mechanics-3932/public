package org.dirtymechanics.frc.component.arm.grabber;

import org.dirtymechanics.frc.actuator.DoubleSolenoid;
import org.dirtymechanics.frc.util.Updatable;

/**
 * This class represents the mechanism used to grab a ball.
 * @author Daniel Ruess
 */
public class GrabberArmPair implements Updatable{

    private final DoubleSolenoid fingers;

    public GrabberArmPair(DoubleSolenoid fingers) {
        this.fingers = fingers;
    }

    public void open() {
        fingers.set(true);
    }

    public void close() {
        fingers.set(false);
    }
    
    public void flip(){
        fingers.flip();
    }

    public void update() {
        fingers.update();
    }
    
    
}
