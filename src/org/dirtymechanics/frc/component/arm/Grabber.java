package org.dirtymechanics.frc.component.arm;

import org.dirtymechanics.frc.actuator.DoubleSolenoid;

/**
 * This class represents the mechanism used to grab a ball.
 * @author Daniel Ruess
 */
public class Grabber {

    private final DoubleSolenoid fingers;

    public Grabber(DoubleSolenoid fingers) {
        this.fingers = fingers;
    }

    public void open() {
        fingers.set(true);
    }

    public void close() {
        fingers.set(false);
    }
}
