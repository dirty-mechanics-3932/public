package org.dirtymechanics.frc.control;

import edu.wpi.first.wpilibj.Joystick;

/**
 *
 * @author Daniel Ruess
 */
public class ButtonMap {

    private double leftSpeed = 0;
    private double rightSpeed = 0;

    private final Joystick left;
    private final Joystick right;
    private final Joystick cont;

    private boolean transmissionState = false;
    private boolean transmissionLast = false;
    private int transmissionFlip = 0;

    private int mode = 0;

    public ButtonMap(Joystick left, Joystick right, Joystick cont) {
        this.left = left;
        this.right = right;
        this.cont = cont;
    }

    public double getDriveLeft() {
        double spd = left.getY();
        double scale = -1;
        if (left.getRawButton(1)) {
            spd = right.getY();
        }
        spd *= scale;
        if (spd > 0) {
            if (spd > leftSpeed) {
                leftSpeed += .0625;
            } else {
                leftSpeed = spd;
            }
        } else {
            if (spd < leftSpeed) {
                leftSpeed -= .0625;
            } else {
                leftSpeed = spd;
            }
        }
        return spd; //leftSpeed;
    }

    public double getDriveRight() {
        double spd = right.getY();
        double scale = -1;
        spd *= scale;
        if (spd > 0) {
            if (spd > rightSpeed) {
                rightSpeed += .0625;
            } else {
                rightSpeed = spd;
            }
        } else {
            if (spd < rightSpeed) {
                rightSpeed -= .0625;
            } else {
                rightSpeed = spd;
            }
        }
        return spd;//rightSpeed;
    }

    public boolean isTransmissionHigh() {
        boolean state = right.getRawButton(1);
        if (true) {
            return state;
        }
        if (state != transmissionLast) {
            transmissionFlip++;
            if (transmissionFlip % 2 == 0) {
                transmissionState = !transmissionState;
            }
            transmissionLast = state;
        }
        return transmissionState;
    }

    public boolean fire() {
        return cont.getRawButton(12);
    }

    /**
     * 0 = idle 1 = firing 2 = collecting
     *
     * @return
     */
    public int getMode() { //TODO: add ball sensor
        if (cont.getRawButton(2)) {
            mode = 1;
        } else if (cont.getRawButton(3)) {
            mode = 2;
        } else {
            if (cont.getRawButton(1)) {
                mode = 0;
            }
        }
        return mode;
    }
}
