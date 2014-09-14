package org.dirtymechanics.frc.component.arm;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import org.dirtymechanics.frc.actuator.DoubleSolenoid;
import org.dirtymechanics.frc.component.arm.ScrewDrive.Location;
import org.dirtymechanics.frc.component.arm.grabber.Grabber;
import org.dirtymechanics.frc.util.Updatable;

/**
 * Manages the firing pin and screw drive ONLY.
 *
 */
public class Shooter implements Updatable {
    Roller roller;
    Grabber grabber;

    private static final int FIRE_WAIT = 250;


    /**
     * Whether or not it fired.
     */
    private boolean fired = false;
    /**
     * The time the mechanism was last fired.
     */
    private long lastFired;
    private final Solenoid firingOpen = new Solenoid(2, 1);
    private final Solenoid firingClose = new Solenoid(2, 2);
    private final DoubleSolenoid firingPin = new DoubleSolenoid(firingOpen, firingClose);
    
    /**
     * @param screw The screw drive.
     * @param firingPin The solenoid for releasing the buckle.
     */
    public Shooter() {
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
     * Updates the state of the firing pin, let others worry about their
     * business.  This is just for the firing pin and the screw drive.
     *
     * Automatically retracts the screw drive after firing.
     */
    public void update() {
        if (fired && doneWaitingToOpen()) {
                firingPin.open();
        }
        if (fired && doneShooting()) {
            firingPin.close();
            fired = false;
        }
        
        
    }

    private boolean doneShooting() {
        return System.currentTimeMillis() - lastFired > (FIRE_WAIT * 2);
    }
    private boolean doneWaitingToOpen() {
        return System.currentTimeMillis() - lastFired > FIRE_WAIT;
    }


}
