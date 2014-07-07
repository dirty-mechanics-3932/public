package org.dirtymechanics.frc.component.drive;

import org.dirtymechanics.frc.actuator.DoubleSolenoid;

/**
 *
 * @author Daniel Ruess
 */
public class Transmission {

    private final DoubleSolenoid solenoid;

    public Transmission(DoubleSolenoid solenoid) {
        this.solenoid = solenoid;
    }

    public void setHigh() {
        solenoid.set(true);
    }

    public void setLow() {
        solenoid.set(false);
    }

}
