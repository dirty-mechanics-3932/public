/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dirtymechanics.frc.control;

import org.dirtymechanics.frc.component.drive.DriveTrain;

/**
 *
 * @author agresh
 */
public class DriveControl {
    private DriverLeftStick left;
    private DriverRightStick right;
    private DriveTrain driveTrain;
    
    public DriveControl(DriverLeftStick left, DriverRightStick right, DriveTrain driveTrain) {
        this.left = left;
        this.right = right;
        this.driveTrain = driveTrain;
    }
        
    public void setSpeed() {
        if (left.getRawButton(1)) {
            double spd = right.getY();
            driveTrain.setSpeed(left.updateDriveSpeed(spd), right.updateDriveSpeed());
        } else {
            driveTrain.setSpeed(left.updateDriveSpeed(), right.updateDriveSpeed());
            left.updateDriveSpeed();
            right.updateDriveSpeed();
        }
        
    }
    
    

    public boolean isTransmissionHigh() {
        return right.isTransmissionHigh();
    }
        
    
    
}
