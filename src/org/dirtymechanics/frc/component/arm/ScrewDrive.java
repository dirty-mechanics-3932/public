package org.dirtymechanics.frc.component.arm;

import edu.wpi.first.wpilibj.Jaguar;
import org.dirtymechanics.frc.sensor.StringEncoder;
import org.dirtymechanics.frc.util.Updatable;

/**
 * A controller for a screw drive.
 *
 * @author Daniel Ruess
 */
public class ScrewDrive implements Updatable {

    public static final Location TRUSS_SHOT = new Location(2.168);
    public static final Location PASS = new Location(1.5);
    public static final Location HIGH_GOAL = new Location(2.168); //2.55
    public static final Location AUTONOMOUS_SHOT = HIGH_GOAL;//new Location(2.45);
    public static final Location RESET = new Location(0.45); //(0.577); 
    /**
     * Represents a location to move the screw drive to.
     */
    public static class Location {

        private final double loc;

        private Location(double loc) {
            this.loc = loc;
        }
    }
    /**
     * The default speed to run at.
     */
    private static final double SPEED = 1D; //TODO: calculate this.

    private final Jaguar motor;
    private final StringEncoder string;
    private double dest;
    private int speedScale = 0;

    public ScrewDrive(Jaguar motor, StringEncoder string) {
        this.motor = motor;
        this.string = string;
        set(PASS);
    }

    /**
     *
     * @param destination
     */
    public final void set(Location destination) {
        // if (resetting) {
        //    nextDestination = destination;
        //} else {
        this.dest = destination.loc;
        //}
    }

    public void increaseOffset() {
        dest += .1;
    }

    public void decreaseOffset() {
        dest -= .1;
    }

    public int getPosition() {
        return string.getDistance();
    }

    public void update() {
        double loc = string.getAverageVoltage();
        double dif = Math.abs(dest - loc);
        double error = .01;

        if (dif > error) {
            if (dest < loc) {
                if (dif < .1) {
                    motor.set(.4);
                } else {
                    motor.set(SPEED);
                }
            } else {
                if (dif < .07) {
                    motor.set(0);
                } else {
                    motor.set(-1 * SPEED);
                }
            }
        } else {
            motor.set(0);
            if (dest == RESET.loc) {
                set(HIGH_GOAL);
            }
        }
        //System.out.println("stringEncoder = " + loc + " dest = " + dest);
    }
}
