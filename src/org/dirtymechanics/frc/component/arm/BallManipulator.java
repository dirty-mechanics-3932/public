/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dirtymechanics.frc.component.arm;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
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
    
    
    
    private long octoTime;
    private boolean octoSwitchClosed;

    
    
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
    

    FireControl fireControl;
    
    
    
    
    public BallManipulator() {
    }

    public void init() {
        screwDrive.init();
        roller.init();
        boom.init();
        shooter.init();
        fireControl = new FireControl(operatorController, this);
        
    }
    
    public void setType(RobotType robotType) {
        server.putString("Robot Type", robotType==RobotType.WOOLLY ? "Woolly" : "Sibling");
        if (robotType.equals(RobotType.WOOLLY)) {
            boom = new PIDBoom(boomMotor, rotEncoder, operatorJoy, operatorController);
            grabber = new WoollyGrabber(operatorController);
            screwDrive.setProperties(new ScrewPropsWoolly());
        } else {
            boom = new PIDBoomSibling(boomMotor, rotEncoder, operatorJoy, operatorController);
            grabber = new SiblingGrabber(operatorController);
            screwDrive.setProperties(new ScrewPropsSibling());
        }   shooter = new Shooter(); // Shouldn't this be initialized for Woolly as well?
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

    
    //TODO move this stuff into grabber
    public void updateOcto() {
        server.putBoolean("OctoSwitch.ballDetected", octoSwitchClosed);
        server.putBoolean("Graber.largeOpen", grabber.isOpenLarge());
        server.putBoolean("Graber.smallOpen", grabber.isOpenSmall());
        updateOctoSwitch();
        final long timeSinceOctoSwitchOpen = System.currentTimeMillis() - octoTime;
        if (octoSwitchClosed && timeSinceOctoSwitchOpen < 1000) {        
 
            if (timeSinceOctoSwitchOpen > 600) {
                roller.closeArm();
            }
            
            if (timeSinceOctoSwitchOpen > 250) {               
                grabber.closeLarge();
                grabber.closeSmall();
            }
        }
    }

    public void updateOctoSwitch() {
        if (isBallSwitchOpen()) {
            if (!octoSwitchClosed) {
                octoSwitchClosed = true;
                octoTime = System.currentTimeMillis();
            }
        } else {
            octoSwitchClosed = false;
        }
    }

    public void update() {
        
        grabber.update();
        shooter.update();
        screwDrive.update();
        roller.update();
        updateOcto();
        boom.update();
        
        fireControl.update();
        
        server.putNumber("Ultra Distance", ultrasonicSensor.getAverageVoltage());
    }
    
    
    


    /**
     * Called from event handler once when button is pressed
     * @param time 
     */
    public void startImmediateFiringSequance(long time) {
       fireControl.startFiringSequence(time);
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

    void resetScrewDrive() {
        screwDrive.reset();
    }

    void openRollerArm() {
        roller.openArm();
    }

    void openSmall() {
        grabber.openSmall();
    }

    void openFire() {
        System.out.println("depressing seatbelt");
        shooter.fire();
    }

    public void shootAutonomous(long time) {
        fireControl.shootAutonomous(time);
    }
   
    
}
