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
public class OperatorJoystick extends edu.wpi.first.wpilibj.Joystick implements BasicJoystick, GameController {
    public static final int INCREASE_BOOM_OFFSET_BUTTON = 6;
    public static final int DECREASE_BOOM_OFFSET_BUTTON = 4;
    public static final int SCREW_DRIVE_AXIS = 6;
    public static final double HALF_JOYSTICK_THROW_RANGE = .5;
    
    public OperatorJoystick(int port) {
        super(port);
    }

    public boolean isIncreaseBoomOffsetPressed() {
        return getRawButton(INCREASE_BOOM_OFFSET_BUTTON);
    }
    
    public boolean isDecreaseBoomOffsetPressed() {
        return getRawButton(DECREASE_BOOM_OFFSET_BUTTON);
    }
    
    public boolean isScrewDriveIncreaseOffsetPressed() {
        return getRawAxis(SCREW_DRIVE_AXIS) < -HALF_JOYSTICK_THROW_RANGE;
    }

    public boolean isScrewDriveDecreaseOffsetPressed() {
        return getRawAxis(SCREW_DRIVE_AXIS) > .5;
    }
    
    
    
    
}