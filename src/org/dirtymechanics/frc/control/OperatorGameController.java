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
public class OperatorGameController extends edu.wpi.first.wpilibj.Joystick implements BasicJoystick, GameController {
    public static final int OPERATOR_CONTROLLER_USB_PORT = 3;
    
    public static final int PASS_BUTTON = 2;
    public static final int GROUND_BUTTON = 3;
    public static final int REST_BUTTON = 1;
    public static final int HIGH_GOAL_BUTTON = 4;
    public static final int ROLLER_ARM_JOY_BUTTON = 5;
    public static final int FIRE_BUTTON = 6;
    public static final int SMALL_GRABBER_JOY_BUTTON = 7;
    public static final int LARGE_GRABBER_JOY_BUTTON = 8;
    public static final int ROLLER_REVERSE_JOY_BUTTON = 9;
    public static final int ROLLER_FORWARD_JOY_BUTTON = 10;
    //TODO It would be great to have a self diagnostic that spit out
    //all of the values for the controls.
    
    

    public OperatorGameController(int port) {
        super(port);
    }

    public boolean isLargeGrabberButtonPressed() {
        return getRawButton(LARGE_GRABBER_JOY_BUTTON);
    }

    public boolean isSmallGrabberButtonPressed() {
        return getRawButton(SMALL_GRABBER_JOY_BUTTON);
    }

    public boolean isRollerArmButtonPressed() {
        return getRawButton(ROLLER_ARM_JOY_BUTTON);
    }

    public boolean isRollerReverseButtonPressed() {
        return getRawButton(ROLLER_REVERSE_JOY_BUTTON);
    }

    public boolean isRollerForwardButtonPressed() {
        return getRawButton(ROLLER_FORWARD_JOY_BUTTON);
    }

    public boolean isHighGoalButtonPressed() {
        return getRawButton(HIGH_GOAL_BUTTON);
    }
    
    public boolean isPassButtonPressed() {
        return getRawButton(PASS_BUTTON);
    }
    
    public boolean isRestButtonPressed() {
        return getRawButton(REST_BUTTON);
    }    

    public boolean isGroundButtonPressed() {
        return getRawButton(GROUND_BUTTON);
    }

    public boolean isScrewDriveResetPressed() {
        return getRawAxis(5) < -.5;
    }
    
    public boolean isScrewDriveTrussPressed() {
        return getRawAxis(5) > .5;
    }
    
    public boolean isScrewDrivePassPressed() {
        return getRawAxis(6) > .5;
    }
    
    public boolean isScrewDriveHighPressed() {
        return getRawAxis(6) < -.5;
    }
    
    public boolean isScrewDriveIncreaseOffsetPressed() {
        return getRawAxis(6) < -.5;
    }

    public boolean isFireButtonPressed() {
        return getRawButton(FIRE_BUTTON);
    }
    
    
    
    
    
}
