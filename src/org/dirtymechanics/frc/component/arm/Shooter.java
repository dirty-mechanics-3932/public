package org.dirtymechanics.frc.component.arm;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import org.dirtymechanics.frc.actuator.DoubleSolenoid;
import org.dirtymechanics.frc.component.arm.ScrewDrive.Location;
import org.dirtymechanics.frc.component.arm.grabber.Grabber;
import org.dirtymechanics.frc.util.Updatable;

/**
 * Represents the mechanism used to fire the ball of the robot.
 *
 * @author Daniel Ruess
 */
public class Shooter implements Updatable {
    Roller roller;
    Grabber grabber;

    private static final int FIRE_WAIT = 250;

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
    public Shooter(ScrewDrive screw, DoubleSolenoid firingPin, Grabber grabber, Roller roller) {
        this.screw = screw;
        this.firingPin = firingPin;
        //This isn't the best abstraction for this, just trying to get things working.
        this.grabber = grabber;
        this.roller = roller;
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
        fired = true;
        //start timings for firing cycle
        lastFired = System.currentTimeMillis();
    }
    
    

    /**
     * Updates the state of the firing pin.
     *
     * Automatically retracts the screw drive after firing.
     */
    public void update() {
        if (fired && !doneWaitingToOpen()) {
                grabber.openLarge();
                grabber.openSmall();
                roller.openArm();
                roller.stop();
            
        }
        if (fired && doneWaitingToOpen()) {
                firingPin.set(true);
        }
        if (fired && doneShooting()) {
            firingPin.set(false);
            fired = false;
            set(ScrewDrive.RESET);
        }
        
        
    }

    private boolean doneShooting() {
        return System.currentTimeMillis() - lastFired > (FIRE_WAIT * 2);
    }
    private boolean doneWaitingToOpen() {
        return System.currentTimeMillis() - lastFired > FIRE_WAIT;
    }
}
