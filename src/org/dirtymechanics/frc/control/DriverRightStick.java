/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dirtymechanics.frc.control;

/**
 *
 * @author agresh
 */
public class DriverRightStick extends DriverStick implements BasicJoystick {
    private boolean transmissionState = false;
    private boolean transmissionLast = false;
    private int transmissionFlip = 0;

    public DriverRightStick(int port) {
        super(port);
    }
    
    public boolean isTransmissionHigh() {
        boolean state = getRawButton(1);
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
    
    public double updateDriveSpeed() {
        double spd = getY();
        return updateDriveSpeed(spd);
    }
    
}
