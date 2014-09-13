/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dirtymechanics.frc.component.arm;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import org.dirtymechanics.event.ButtonListener;
import org.dirtymechanics.frc.FireButtonEventHandler;
import org.dirtymechanics.frc.RobotType;
import org.dirtymechanics.frc.actuator.DoubleSolenoid;
import org.dirtymechanics.frc.component.arm.grabber.Grabber;
import org.dirtymechanics.frc.component.arm.grabber.SiblingGrabber;
import org.dirtymechanics.frc.component.arm.grabber.WoollyGrabber;
import org.dirtymechanics.frc.control.BasicJoystick;
import org.dirtymechanics.frc.control.GameController;
import org.dirtymechanics.frc.control.Joystick;
import org.dirtymechanics.frc.sensor.MaxBotixMaxSonarEZ4;
import org.dirtymechanics.frc.sensor.RotationalEncoder;
import org.dirtymechanics.frc.sensor.StringEncoder;
import org.dirtymechanics.frc.util.Updatable;

public class BallManipulator implements Updatable {
       /**
     * Jaguar controlling the screw drive.
     */
    private final Jaguar screwMotor = new Jaguar(6);
    /**
     * Jaguar controlling the boom.
     */
    private final Talon boomMotor = new Talon(5);
    /**
     * Jaguar controller the grabber's roller.
     */
    public final Relay rollerMotor = new Relay(2);
    /**
     * The string encoder used for the screw drive.
     */
    public final StringEncoder stringEncoder = new StringEncoder(1);
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
        
    
    private PIDBoom angleControlAssembly;
    private ScrewDrive screDrive;
    private final Solenoid firingOpen = new Solenoid(2, 1);
    private final Solenoid firingClose = new Solenoid(2, 2);
    private final DoubleSolenoid firingSolenoid = new DoubleSolenoid(firingOpen, firingClose);
    public final ScrewDrive screwDrive = new ScrewDrive(screwMotor, stringEncoder);
    private Solenoid rollerOpen = new Solenoid(1, 3);
    private Solenoid rollerClose = new Solenoid(1, 4);
    private DoubleSolenoid rollerSolenoid = new DoubleSolenoid(rollerOpen, rollerClose);
    public Roller roller = new Roller(rollerMotor, rollerSolenoid);
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
    
    
    public static final int OPERATOR_CONTROLLER_USB_PORT = 3;
    public GameController operatorController = new Joystick(OPERATOR_CONTROLLER_USB_PORT);
    public static final int OPERATOR_JOY_USB_PORT = 4;
    private BasicJoystick operatorJoy = new Joystick(OPERATOR_JOY_USB_PORT);
    
    FireButtonEventHandler fireButtonHandler;
    public ButtonListener fireButtonListener;
    
    public BallManipulator() {
        //FIXME angleControlAssembly and screwDrive assignments got lost while
        //moving things so they are never initialized.
    }

    public void init() {
        fireButtonHandler = new FireButtonEventHandler(operatorController, this);
        fireButtonListener = new ButtonListener();
        fireButtonListener.addListener(fireButtonHandler);
    }
    
    public void update() {
        grabber.update();
        firingSolenoid.update();
        rollerSolenoid.update();
        shooter.update();
        screwDrive.update();
        
    }

    public void setType(RobotType robotType) {
        if (robotType == RobotType.WOOLLY) {
            boom = new PIDBoom(boomMotor, rotEncoder);
            grabber = new WoollyGrabber();
        }
        else if (robotType == RobotType.SIBLING){
            boom = new PIDBoomSibling(boomMotor, rotEncoder);
            grabber = new SiblingGrabber();
        }
        shooter = new Shooter(screwDrive, firingSolenoid, grabber, roller);
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
    
    public void updateScrewDrive() {
        if (operatorController.getRawAxis(5) < -.5) {
            screwDrive.set(ScrewDrive.RESET);
        } else if (operatorController.getRawAxis(5) > .5) {
            screwDrive.set(ScrewDrive.TRUSS_SHOT);
        } else if (operatorController.getRawAxis(6) > .5) {
            screwDrive.set(ScrewDrive.PASS);
        } else if (operatorController.getRawAxis(6) < -.5) {
            screwDrive.set(ScrewDrive.HIGH_GOAL);
        }

        if (operatorJoy.getRawAxis(6) < -.5) {
            if (released[20]) {
                screwDrive.increaseOffset();
                released[20] = false;
            }
            released[20] = false;
        } else if (operatorJoy.getRawAxis(6) > .5) {
            if (released[20]) {
                screwDrive.decreaseOffset();
                released[20] = false;
            }
        } else {
            released[20] = true;
        }
    }
    
    public void updateOcto() {
        if (isBallSwitchOpen()) {
            if (!octoSwitchOpen) {
                octoSwitchOpen = true;
                octoTime = System.currentTimeMillis();
            }
        } else {
            octoSwitchOpen = false;
        }

        if (octoSwitchOpen) {
            if (System.currentTimeMillis() - octoTime > 250) {
                if (!operatorController.getRawButton(ROLLER_REVERSE_CTL_GROUP)) {
                    setToggle(ROLLER_REVERSE_CTL_GROUP, false);
                }
                if (System.currentTimeMillis() - octoTime > 600) {
                    setToggle(ROLLER_ARM_CTL_GROUP, false);
                }
                setToggle(LARGE_GRABBER_CTL_GROUP, false);
                setToggle(SMALL_GRABBER_CTL_GROUP, false);
                setToggle(ROLLER_FORWARD_CTL_GROUP, false);
                
                grabber.closeSmall();
            }
        }
    }
    
    public void updateBoom() {
        if (!boom.BOOM_ENABLED) {
            return; //early exit, don't do anything.
        }
        if (operatorJoy.getRawButton(6)) {
            //boomMotor.set(.7);
            if (released[21]) {
                boom.increaseOffset();
                released[21] = false;
            }
        } else if (operatorJoy.getRawButton(4)) {
            //boomMotor.set(-.7);
            if (released[21]) {
                boom.decreaseOffset();
                released[21] = false;
            }
        } else {
            //boomMotor.set(0);
            released[21] = true;
        }

        if (operatorController.getRawButton(4)) {
            boom.set(boom.getBoomProperties().getHighGoal());
        } else if (operatorController.getRawButton(1)) {
            boom.set(boom.getBoomProperties().getPass());
        } else if (operatorController.getRawButton(3)) {
            boom.set(boom.getBoomProperties().getRest());
        } else if (operatorController.getRawButton(2)) {
            boom.set(boom.getBoomProperties().getGround());
            setToggle(SMALL_GRABBER_CTL_GROUP, true);
            setToggle(ROLLER_FORWARD_CTL_GROUP, true);
            setToggle(ROLLER_REVERSE_CTL_GROUP, false);
            
            roller.forward();
//            grabSmallSolenoid.setOpen();
            grabber.openSmall();
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
    
    public void checkArmControls() {
        checkLargeGrabberButton();
        checkSmallGrabberButton();
        checkRollerArmButton();
        checkRollerForwardButton();
        checkRollerReverseButton();
    }

    private void checkRollerReverseButton() {
        //roller rev
        if (operatorController.getRawButton(ROLLER_REVERSE_CTL_GROUP)) {
            if (released[ROLLER_REVERSE_CTL_GROUP]) {
                toggle[ROLLER_REVERSE_CTL_GROUP]++;
                released[ROLLER_REVERSE_CTL_GROUP] = false;
                if (toggle[ROLLER_REVERSE_CTL_GROUP]%2 == 0){
                    server.putString("Wooly.roller", "Reverse");
                    roller.reverse();
                }
                else {
                    roller.stop();
                    server.putString("Wooly.roller", "Stop");
                }
            }
        } else {
            released[ROLLER_REVERSE_CTL_GROUP] = true;
        }
    }

    private void checkRollerForwardButton() {
        //roller forward
        if (operatorController.getRawButton(ROLLER_FORWARD_CTL_GROUP)) {
            if (released[ROLLER_FORWARD_CTL_GROUP]) {
                toggle[ROLLER_FORWARD_CTL_GROUP]++;
                released[ROLLER_FORWARD_CTL_GROUP] = false;
                if (toggle[ROLLER_FORWARD_CTL_GROUP]%2 == 0) {
                    roller.forward();
                    server.putString("Wooly.roller", "Forward");
                }
                else {
                    server.putString("Wooly.roller", "Stop");
                    roller.stop();
                }
            }
        } else {
            released[ROLLER_FORWARD_CTL_GROUP] = true;
        }
    }

    private void checkRollerArmButton() {
        final boolean rollerArmButtonPressed = operatorController.getRawButton(ROLLER_ARM_CTL_GROUP);
        //roller arm
        if (rollerArmButtonPressed) {
            if (released[ROLLER_ARM_CTL_GROUP]) {
                toggle[ROLLER_ARM_CTL_GROUP]++;
                released[ROLLER_ARM_CTL_GROUP] = false;
                if (toggle[ROLLER_ARM_CTL_GROUP]%2 != 0) {
                    server.putString("Wooly.rollerArm", "Open");
                    roller.openArm();
                }
                else {
                    server.putString("Wooly.rollerArm", "Close");
                    roller.closeArm();
                }
            }
        } else {
            released[ROLLER_ARM_CTL_GROUP] = true;
            toggle[ROLLER_ARM_CTL_GROUP]++;
        }
    }

    private void checkSmallGrabberButton() {
        //small arm
        if (operatorController.getRawButton(SMALL_GRABBER_CTL_GROUP)) {
            if (released[SMALL_GRABBER_CTL_GROUP]) {
                toggle[SMALL_GRABBER_CTL_GROUP]++;
                grabber.flipSmall();
                released[SMALL_GRABBER_CTL_GROUP] = false;
            }
        } else {
            released[SMALL_GRABBER_CTL_GROUP] = true;
        }
    }
    

    private void checkLargeGrabberButton() {
        //large arm
        if (operatorController.getRawButton(LARGE_GRABBER_CTL_GROUP)) {
            if (released[LARGE_GRABBER_CTL_GROUP]) {
                toggle[LARGE_GRABBER_CTL_GROUP]++;
                released[LARGE_GRABBER_CTL_GROUP] = false;
                grabber.flipLarge();
            }
        } else {
            released[LARGE_GRABBER_CTL_GROUP] = true;
        }
    }    
    
}
