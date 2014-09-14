/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dirtymechanics.frc.component.arm;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import org.dirtymechanics.event.impl.ButtonListener;
import org.dirtymechanics.frc.component.arm.event.FireButtonEventHandler;
import org.dirtymechanics.frc.RobotType;
import org.dirtymechanics.frc.component.arm.grabber.Grabber;
import org.dirtymechanics.frc.component.arm.grabber.SiblingGrabber;
import org.dirtymechanics.frc.component.arm.grabber.WoollyGrabber;
import org.dirtymechanics.frc.control.OperatorGameController;
import org.dirtymechanics.frc.control.OperatorJoystick;
import org.dirtymechanics.frc.sensor.MaxBotixMaxSonarEZ4;
import org.dirtymechanics.frc.sensor.RotationalEncoder;
import org.dirtymechanics.frc.util.Updatable;

public class BallManipulator implements Updatable {
    NetworkTable server = NetworkTable.getTable("SmartDashboard");
    
    private final Talon boomMotor = new Talon(5);
    
    //TODO rename to more descriptive name and get rid of comments.
    /**
     * The string encoder used for the screw drive.
     */
    
    /**
     * The rotational encoder used for the boom.
     */
    public final RotationalEncoder rotEncoder = new RotationalEncoder(2);
    
    public boolean firing;
    private long fireButtonPressTime;
    public long actualFireTime;
    private long octoTime;
    private boolean octoSwitchOpen;
    String firingStatus = "";
    private boolean fired;
    
    boolean isImmediate;
    
    
    public MaxBotixMaxSonarEZ4 ultrasonicSensor = new MaxBotixMaxSonarEZ4(3);

    public final DigitalInput octo = new DigitalInput(2);

    /**
     * Configurable via the smart dashboard, controls the opening and
     * closing of the arms.
     */
    public Grabber grabber;
    
    
    public Shooter shooter;
    
    /**
     * Configurable via the smart dashboard, controls the assembly consisting of the encoder and the 
     * motor for rasing and lowering the arm.
     */
    public PIDBoom boom;
    public OperatorGameController operatorController = new OperatorGameController(OperatorGameController.OPERATOR_CONTROLLER_USB_PORT);
    public Roller roller = new Roller(operatorController);
    public final ScrewDrive screwDrive = new ScrewDrive(operatorController);
    public static final int OPERATOR_JOY_USB_PORT = 4;
    private OperatorJoystick operatorJoy = new OperatorJoystick(OPERATOR_JOY_USB_PORT);
    
    FireButtonEventHandler fireButtonHandler;
    ButtonListener fireButtonListener = new ButtonListener();
//    public ButtonListener fireButtonListener;
    private boolean shoot = false;
    
    
    
    
    public BallManipulator() {
    }

    public void init() {
        screwDrive.init();
        fireButtonHandler = new FireButtonEventHandler(operatorController, this);
        fireButtonListener.addHandler(fireButtonHandler);
        boom.init();
        
    }
    
    public void setType(RobotType robotType) {
        if (robotType == RobotType.WOOLLY) {
            boom = new PIDBoom(boomMotor, rotEncoder, operatorJoy, operatorController);
            grabber = new WoollyGrabber(operatorController);
        }
        else if (robotType == RobotType.SIBLING){
            boom = new PIDBoomSibling(boomMotor, rotEncoder, operatorJoy, operatorController);
            grabber = new SiblingGrabber(operatorController);
        }        shooter = new Shooter();
    }
    
    private boolean isArmingRange() {
        return ultrasonicSensor.getRangeInInches() > 100 && ultrasonicSensor.getRangeInInches() < 115;
    }

    public boolean isCorrectRange() {
        return ultrasonicSensor.getRangeInInches() > 75 && ultrasonicSensor.getRangeInInches() < 85;
    }
    
    public boolean isBallSwitchOpen() {
        return !octo.get();
    }

    boolean holdingTheBallAndNotFiring() {
        return !octoSwitchOpen && !firing;
    }
    
    //TODO move this stuff into grabber
    public void updateOcto() {
        updateOctoSwitch();
        if (octoSwitchOpen) {
            if (System.currentTimeMillis() - octoTime > 250) {
                if (!operatorController.isRollerReverseButtonPressed()) {
                    grabber.openLarge();
                    
                }
                if (System.currentTimeMillis() - octoTime > 600) {
                    roller.closeArm();
                }
                grabber.closeSmall();
            }
        }
    }

    public void updateOctoSwitch() {
        if (isBallSwitchOpen()) {
            if (!octoSwitchOpen) {
                octoSwitchOpen = true;
                octoTime = System.currentTimeMillis();
            }
        } else {
            octoSwitchOpen = false;
        }
    }


    
   
  
    
    boolean firingButtonTimerExpired() {
        return System.currentTimeMillis() - fireButtonPressTime > 500;
    }

    public void shootAutonomous(long time) {
        if (!firing) {
            roller.openArm();
//                    grabSmallSolenoid.setOpen();
            grabber.openSmall();
            firing = true;
            fireButtonPressTime = System.currentTimeMillis();
        }
        if ((time > 6300 || System.currentTimeMillis() - fireButtonPressTime > 300)) {
            shooter.fire();
            firing = false;
        }
    }


    public boolean isFireButtonPressed() {
        return operatorController.isFireButtonPressed();
    }
    
    
    
    public void update() {
        fireButtonListener.updateState(isFireButtonPressed(), System.currentTimeMillis());
        grabber.update();
        shooter.update();
        screwDrive.update();
        roller.update();
        updateOcto();
        boom.update();
    }
    
    public void setImmediate(boolean immediate) {
        isImmediate = immediate;
    }
    
    public void setShoot(boolean shoot) {
        this.shoot = shoot;
    }
    
    private void lockonShoot() {
        debug("lockonshoot");
    }

    /**
     * Called from event handler
     * @param time 
     */
    public void shoot(long time) {
       if (isTimeToResetFireControls()) {
            debug("reset");
            resetFireControls();
        } else if (isTimeToFire()) {
            debug("fire");
            fire();

        } else {
            debug("prepare to fire");
            prepareToFire();
        }
    }
    
    void prepareToFire() {
        firingStatus = "preparing to fire";
//        disableToggles();
        roller.openArm();
        grabber.openSmall();
    }
    
    public void fire() {
        firingStatus = "firing!";
        shooter.fire();
        fired = true;
        actualFireTime = System.currentTimeMillis();
        
    }
    
    private boolean isTimeToResetFireControls() {
        final boolean resetDelayExpired = System.currentTimeMillis() - actualFireTime > 250;
        return resetDelayExpired && fired;
    }

    private boolean isTimeToFire() {
        boolean fireDelayExpired = isFireDelayExpired();
        boolean rangeIsCorrect = true; //isCorrectRange();
        return fireDelayExpired && rangeIsCorrect;
    }
    
    private boolean isFireDelayExpired() {
        return System.currentTimeMillis() - fireButtonPressTime > 350;
    }
    
    void resetFireControls() {
        firingStatus = "resetting fire controls";
//        disableToggles();
        firing = false;
        fired = false;
        screwDrive.reset();
    }

    
    private boolean debugEnabled = true;
    public void debug(String debugString) {
        if (debugEnabled) {
            System.out.println(debugString);
        }
    }

    public void rollerForward() {
        roller.forward();
    }

    public void rollerStop() {
        roller.stop();
    }

    public void rollerArmOpen() {
        roller.openArm();
    }

    public void setScrewDriveAutonomous() {
        screwDrive.autonomous(); 
    }

    public void setBoomRest() {
        boom.rest();
    }

    public void setBoomAutonomouseShot() {
        boom.autonomous();
    }

    public void boomGround() {
        boom.ground();
    }
   
    
}
