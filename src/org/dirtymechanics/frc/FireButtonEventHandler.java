
package org.dirtymechanics.frc;

import org.dirtymechanics.event.ButtonEventHandler;
import org.dirtymechanics.event.ButtonListener;
import org.dirtymechanics.frc.component.arm.BallManipulator;
import org.dirtymechanics.frc.control.GameController;


/**
 *
 * Responsible for processing events related to shooting.
 * 
 * This isn't great - it's totally dependent on the robot and vice 
 * versa, but it's a start.
 * 
 * When a button click event is triggered shoot.
 * 
 * When a button hold event is triggered lockon shoot.
 */
public class FireButtonEventHandler implements ButtonEventHandler {
    private GameController controller;
    private BallManipulator ballManipulator;
    boolean isImmediate;
    
    
    private String firingStatus = "";
    
    //For now pull in the whole robot.  Later tease apart what should be in 
    //  common into some middle ground.  Need the operatorController to
    //  check on the "safety" button.  Need the robot to call all the things
    //  to do to shoot.
    public FireButtonEventHandler(GameController operatorController, BallManipulator robot) {
        this.controller = operatorController;
        this.ballManipulator = robot;
    }

    public void onEvent(int buttonEvent) {
        debug(firingStatus);
        switch (buttonEvent) {
            case ButtonListener.SINGLE_CLICK:
                isImmediate = true;
                shoot(true);
                debug("click");
                break;
                
            case ButtonListener.HOLD:
                isImmediate = false;
                shoot(false);
                debug("hold");
                break;
            case ButtonListener.NEUTRAL:
                break;
        }
    }

    private void shoot(boolean isImmediate) {
        startFiringSequence();
    }
    
    private void lockonShoot() {
        debug("lockonshoot");
    }

    void startFiringSequence() {
        firingStatus = "starting firing sequence";
        if (isSafeToFire()) {
            firingStatus = "starting firing sequence safety off";
            //TODO this is backwards - ball manipulator should ask this if firing
            ballManipulator.firing = true;
        }
    }

    public boolean isSafeToFire() {
        //return true;  //pressing the fire button WILL fire the mechanism
        return ballManipulator.operatorController.getRawButton(11) || ballManipulator.isBallSwitchOpen();
    }
    
    private boolean debugEnabled = true;
    public void debug(String debugString) {
        if (debugEnabled) {
            System.out.println(debugString);
        }
    }

    
}
