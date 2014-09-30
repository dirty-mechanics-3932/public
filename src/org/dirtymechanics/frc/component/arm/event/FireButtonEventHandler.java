
package org.dirtymechanics.frc.component.arm.event;

import org.dirtymechanics.event.ButtonEventHandler;
import org.dirtymechanics.event.impl.ButtonListener;
import org.dirtymechanics.frc.component.arm.BallManipulator;
import org.dirtymechanics.frc.control.GameController;


/**
 *
 * Responsible for processing events related to shooting, requires that
 * updateState be during the teleopPeriodic loop.
 * 
 * 
 * When a button click event is triggered shoot.
 * 
 * When a button hold event is triggered lockon shoot.
 */
public class FireButtonEventHandler implements ButtonEventHandler {
    private GameController controller;
    private BallManipulator ballManipulator;
    
    
    //For now pull in the whole robot.  Later tease apart what should be in 
    //  common into some middle ground.  Need the operatorController to
    //  check on the "safety" button.  Need the robot to call all the things
    //  to do to shoot.
    public FireButtonEventHandler(GameController operatorController, BallManipulator robot) {
        this.controller = operatorController;
        this.ballManipulator = robot;
    }

    public void onEvent(int buttonEvent) {
        switch (buttonEvent) {
            case ButtonListener.SINGLE_CLICK:
                ballManipulator.startImmediateFiringSequence(System.currentTimeMillis());
                break;
                
            case ButtonListener.HOLD:
//                ballManipulator.setImmediate(false);
//                ballManipulator.setShoot(false);
                break;
            case ButtonListener.NEUTRAL:
                break;
        }
    }
    
}
