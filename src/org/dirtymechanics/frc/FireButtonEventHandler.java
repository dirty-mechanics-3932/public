
package org.dirtymechanics.frc;

import edu.wpi.first.wpilibj.Joystick;
import org.dirtymechanics.event.ButtonEventHandler;
import org.dirtymechanics.event.ButtonListener;
import org.dirtymechanics.frc.component.arm.PIDBoom;


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
    private Joystick controller;
    private Woolly robot;
    boolean isImmediate;
    
    
    private String firingStatus = "";
    private boolean fired;
    
    //For now pull in the whole robot.  Later tease apart what should be in 
    //  common into some middle ground.  Need the operatorController to
    //  check on the "safety" button.  Need the robot to call all the things
    //  to do to shoot.
    public FireButtonEventHandler(Joystick operatorController, Woolly robot) {
        this.controller = operatorController;
        this.robot = robot;
    }

    public void onEvent(int buttonEvent) {
        switch (buttonEvent) {
            case ButtonListener.SINGLE_CLICK:
                isImmediate = true;
                shoot(true);
                break;
            case ButtonListener.HOLD:
                isImmediate = false;
                shoot(false);
                break;
            case ButtonListener.NEUTRAL:
//                resetFireControls();
                break;
        }
    }

    private void shoot(boolean isImmediate) {
        startFiringSequence();
    }
    
    private void lockonShoot() {
        System.out.println("lockonshoot");
    }

    void startFiringSequence() {
        firingStatus = "starting firing sequence";
        //if (released[6]) {
        if (isSafeToFire()) {
            firingStatus = "starting firing sequence safety off";
            robot.firing = true;
        }
        //}
    }

    private boolean isSafeToFire() {
        //return true;  //pressing the fire button WILL fire the mechanism
        return robot.operatorController.getRawButton(11) || robot.isBallSwitchOpen();
    }
    
    public void fire(boolean isCorrectRange) {
        firingStatus = "firing!";
        System.out.println("wanted to fire at range " + robot.ultrasonicSensor.getRangeInInches());
        if (isImmediate || isCorrectRange) {
            System.out.println("fired at range " + robot.ultrasonicSensor.getRangeInInches());
            robot.shooter.fire();
            fired = true;
            robot.actualFireTime = System.currentTimeMillis();
        } else {
            System.out.println("did NOT fire at range " + robot.ultrasonicSensor.getRangeInInches() + "isImmediate=" + isImmediate + " isCorrectRange=" + isCorrectRange);
        }
    }
    
//    public void prepareToFire() {
//        if (isImmediate) {
//            firingStatus = "preparing to fire";
//            robot.disableToggles();
////            robot.smallGrabber.close();
//            robot.grabber.closeSmall();
////            robot.grabLargeSolenoid.set(false);
//            robot.grabber.closeLarge();
////            robot.grabSmallSolenoid.set(false);
//            robot.roller.openArm();
//            robot.roller.stop();
//        } else {
//            prepareToFireAtAngle();
//        }
//    }
//
//    public void prepareToFireAtAngle() {
//        firingStatus = "preparing to fire at angle";
//        robot.disableToggles();
//        robot.boom.set(robot.boom.getBoomProperties().getHighGoal());
////        robot.grabLargeSolenoid.set(false);
//        robot.grabber.closeLarge();
////        robot.grabSmallSolenoid.set(false);
//        robot.grabber.closeSmall();
//    }

//    void resetFireControls() {
//        firingStatus = "resetting fire controls";
////        robot.grabLargeSolenoid.set(false);
//        robot.grabber.closeLarge();
////        robot.grabSmallSolenoid.set(false);
//        robot.grabber.closeSmall();
//        robot.roller.closeArm();
//        robot.roller.stop();
//        robot.firing = false;
//        fired = false;
//    }
    
}