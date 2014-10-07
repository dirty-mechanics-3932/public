/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dirtymechanics.frc.control;

import edu.wpi.first.wpilibj.Jaguar;
import org.dirtymechanics.frc.component.drive.DriveTrain;
import org.dirtymechanics.frc.util.Updatable;

/**
 *
 * @author agresh
 */
public class DriveControl implements Updatable {
//    private DriverLeftStick left;
//    private DriverRightStick right;
    private final DriverLeftStick left;
    private final DriverRightStick right;
    /**
     * Jaguar that's driving the first left motor.
     */
    private final Jaguar leftDriveMotorA = new Jaguar(1);
    /**
     * Jaguar that's driving the second left motor.
     */
    private final Jaguar leftDriveMotorB = new Jaguar(2);
    /**
     * Jaguar that's driving the first right motor.
     */
    private final Jaguar rightDriveMotorA = new Jaguar(3);
    /**
     * Jaguar that's driving the second right motor.
     */
    private final Jaguar rightDriveMotorB = new Jaguar(4);
    private final DriveTrain driveTrain = new DriveTrain(leftDriveMotorA, leftDriveMotorB, rightDriveMotorA, rightDriveMotorB);
    
    
    public DriveControl(DriverLeftStick left, DriverRightStick right) {
        this.left = left;
        this.right = right;
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
    
    public void setRawSpeed(double leftSpeed, double rightSpeed){
        driveTrain.setLeftSpeed(leftSpeed);
        driveTrain.setRightSpeed(rightSpeed);
    }

    public boolean isTransmissionHigh() {
        return right.isTransmissionHigh();
    }

    public void update() {
        driveTrain.update();
    }

    public void setSpeed(double leftSpeed, double rightSpeed) {
        left.updateDriveSpeed(leftSpeed);
        right.updateDriveSpeed(rightSpeed);
        
    }
        
    
    
}
