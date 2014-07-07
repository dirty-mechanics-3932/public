package org.dirtymechanics.frc.component.arm;

import org.dirtymechanics.frc.actuator.DoubleSolenoid;
import org.dirtymechanics.frc.component.arm.ScrewDrive.Location;
import org.dirtymechanics.frc.util.Updatable;

/**
 * Represents the mechanism used to fire the ball of the robot.
 *
 * @author Daniel Ruess
 */
public class Shooter implements Updatable {

    private static final int FIRE_WAIT = 1000;

    /**
     * The screw drive.
     */
    private final ScrewDrive screw;
    /**
     * The solenoid used to release the button.
     */
    private final DoubleSolenoid firingPin;
    /**
     * Whether or not it fired.
     */
    private boolean fired = false;
    /**
     * The time the mechanism was last fired.
     */
    private long lastFired;

    /**
     * @param screw The screw drive.
     * @param firingPin The solenoid for releasing the buckle.
     */
    public Shooter(ScrewDrive screw, DoubleSolenoid firingPin) {
        this.screw = screw;
        this.firingPin = firingPin;
    }

    /**
     * Sets the location to move the screw drive to.
     *
     * @param dest The destination.
     */
    public void set(Location dest) {
        screw.set(dest);
    }

    /**
     * Fires the firing pin.
     */
    public void fire() {
        firingPin.set(true);
        fired = true;
        lastFired = System.currentTimeMillis();
    }

    /**
     * Updates the state of the firing pin.
     *
     * Automatically retracts the screw drive after firing.
     */
    public void update() {
        if (System.currentTimeMillis() - lastFired > FIRE_WAIT) {
            if (fired) {
                firingPin.set(false);
                fired = false;
                set(ScrewDrive.RESET);
            }
        }
    }
}
