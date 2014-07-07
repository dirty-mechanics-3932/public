package org.dirtymechanics.frc.component.arm;

import edu.wpi.first.wpilibj.Relay;
import org.dirtymechanics.frc.actuator.DoubleSolenoid;

/**
 *
 * @author Daniel Ruess
 */
public class Roller {

    private final Relay motor;
    private final DoubleSolenoid solenoid;

    public Roller(Relay motor, DoubleSolenoid solenoid) {
        this.motor = motor;
        this.solenoid = solenoid;
    }

    public void forward() {
        motor.set(Relay.Value.kForward);
    }

    public void stop() {
        motor.set(Relay.Value.kOff);

    }

    public void reverse() {
        motor.set(Relay.Value.kReverse);
    }

    public void openArm() {
        solenoid.set(true);
    }

    public void closeArm() {
        solenoid.set(false);
    }
}
