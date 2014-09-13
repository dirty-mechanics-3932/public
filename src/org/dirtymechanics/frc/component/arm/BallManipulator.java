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
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import org.dirtymechanics.event.ButtonListener;
import org.dirtymechanics.frc.FireButtonEventHandler;
import org.dirtymechanics.frc.RobotType;
import org.dirtymechanics.frc.actuator.DoubleSolenoid;
import org.dirtymechanics.frc.component.arm.grabber.Grabber;
import org.dirtymechanics.frc.component.arm.grabber.SiblingGrabber;
import org.dirtymechanics.frc.component.arm.grabber.WoollyGrabber;
import org.dirtymechanics.frc.control.OperatorGameController;
import org.dirtymechanics.frc.control.OperatorJoystick;
import org.dirtymechanics.frc.sensor.MaxBotixMaxSonarEZ4;
import org.dirtymechanics.frc.sensor.RotationalEncoder;
import org.dirtymechanics.frc.sensor.StringEncoder;
import org.dirtymechanics.frc.util.Updatable;

public class BallManipulator implements Updatable {
    NetworkTable server = NetworkTable.getTable("SmartDashboard");
    
    Toggle largeGrabberToggle = new Toggle("LargeGrabber");
    Toggle smallGrabberToggle = new Toggle("SmallGrabber");
    Toggle rollerArmToggle = new Toggle("RollerArm");
    Toggle rollerForwardToggle = new Toggle("RollerForward");
    Toggle rollerReverseToggle = new Toggle("RollerReverse");
    Toggle boomToggle = new Toggle("BoomIncrement");
    Toggle screwDriveToggle = new Toggle("ScrewDriveIncrement");
    
    
    private final Jaguar screwMotor = new Jaguar(6);
    private final Talon boomMotor = new Talon(5);
    public final Relay rollerMotor = new Relay(2);
    //TODO rename to more descriptive name and get rid of comments.
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
    public OperatorGameController operatorController = new OperatorGameController(OperatorGameController.OPERATOR_CONTROLLER_USB_PORT);
    public static final int OPERATOR_JOY_USB_PORT = 4;
    private OperatorJoystick operatorJoy = new OperatorJoystick(OPERATOR_JOY_USB_PORT);
    
    FireButtonEventHandler fireButtonHandler;
    public ButtonListener fireButtonListener;
    
    public BallManipulator() {
    }

    public void init() {
        fireButtonHandler = new FireButtonEventHandler(operatorController, this);
        fireButtonListener = new ButtonListener();
        fireButtonListener.addListener(fireButtonHandler);
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
        if (operatorController.isScrewDriveResetPressed()) {
            screwDrive.set(ScrewDrive.RESET);
        } else if (operatorController.isScrewDriveTrussShotPressed()) {
            screwDrive.set(ScrewDrive.TRUSS_SHOT);
        } else if (operatorController.isScrewDrivePassPressed()) {
            screwDrive.set(ScrewDrive.PASS);
        } else if (operatorController.isScrewDriveHighGoalPressed()) {
            screwDrive.set(ScrewDrive.HIGH_GOAL);
        }

        if (operatorJoy.isScrewDriveIncreaseOffsetPressed()) {
            if (!screwDriveToggle.isReleased()) {
                screwDrive.increaseOffset();
                screwDriveToggle.setReleased(false);
            }
            screwDriveToggle.setCurrentState(false);
            
        } else if (operatorJoy.isScrewDriveDecreaseOffsetPressed()) {
            if (!screwDriveToggle.isReleased()) {
                screwDrive.decreaseOffset();
                screwDriveToggle.setReleased(false);
            }
        } else {
            screwDriveToggle.setReleased(true);
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
                if (!operatorController.isRollerReverseButtonPressed()) {
                    largeGrabberToggle.setCurrentState(false);
                }
                if (System.currentTimeMillis() - octoTime > 600) {
                    rollerArmToggle.setCurrentState(false);
                }
                largeGrabberToggle.setCurrentState(false);
                smallGrabberToggle.setCurrentState(false);
                rollerForwardToggle.setCurrentState(false);
                
                grabber.closeSmall();
            }
        }
    }
    
 
    public void updateBoom() {
        if (!boom.BOOM_ENABLED) {
            return; //early exit, don't do anything.
        }
        doIncrementalBoomOffset();  //<--probably broken

        if (operatorController.isHighGoalButtonPressed()) {
            boom.set(boom.getBoomProperties().getHighGoal());
        } else if (operatorController.isPassButtonPressed()) {
            boom.set(boom.getBoomProperties().getPass());
        } else if (operatorController.isRestButtonPressed()) {
            boom.set(boom.getBoomProperties().getRest());
        } else if (operatorController.isGroundButtonPressed()) {
            boom.set(boom.getBoomProperties().getGround());
            smallGrabberToggle.setCurrentState(true);
            rollerForwardToggle.setCurrentState(true);
            rollerReverseToggle.setCurrentState(false);
            roller.forward();
//            grabSmallSolenoid.setOpen();
            grabber.openSmall();
        }
    }

    


    



     //TODO I don't think this code was working due to the fact that it
    //was waiting for presses instead of releases and the intermingled
    //toggles/confused logic.
    void doIncrementalBoomOffset() {
        if (operatorJoy.isIncreaseBoomOffsetPressed()) {
            //boomMotor.set(.7);
            if (boomToggle.isReleased()) {
                boom.increaseOffset();
                boomToggle.setReleased(false);
            }
        } else if (operatorJoy.isDecreaseBoomOffsetPressed()) {
            //boomMotor.set(-.7);
            if (boomToggle.isReleased()) {
                boom.decreaseOffset();
                boomToggle.setReleased(false);
            }
        } else {
            boomToggle.setReleased(true);
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
    

    private void doRollerReverse() {
        //roller rev
        if (operatorController.isRollerReverseButtonPressed()) {
            if (rollerReverseToggle.isReleased()) {
                rollerReverseToggle.incrementPresses();
                rollerReverseToggle.setReleased(false);
                if (!rollerReverseToggle.isOddNumberOfPresses()){
                    server.putString("Wooly.roller", "Reverse");
                    roller.reverse();
                }
                else {
                    roller.stop();
                    server.putString("Wooly.roller", "Stop");
                }
            }
        } else {
            rollerReverseToggle.setReleased(true);
        }
    }

    

    private void doRollerForward() {
        //roller forward
        if (operatorController.isRollerForwardButtonPressed()) {
            if (rollerForwardToggle.isReleased()) {
                rollerForwardToggle.incrementPresses();
                rollerForwardToggle.setCurrentState(false);
                
                if (!rollerForwardToggle.isOddNumberOfPresses()) {
                    roller.forward();
                    server.putString("Wooly.roller", "Forward");
                }
                else {
                    server.putString("Wooly.roller", "Stop");
                    roller.stop();
                }
            }
        } else {
            rollerForwardToggle.setReleased(true);
        }
    }



    private void doRollerArm() {
        if (operatorController.isRollerArmButtonPressed()) {
            if (rollerArmToggle.isReleased()) {
                rollerArmToggle.incrementPresses();
                rollerArmToggle.setReleased(false);
                if (rollerArmToggle.isOddNumberOfPresses()) {
                    server.putString("Wooly.rollerArm", "Open");
                    roller.openArm();
                }
                else {
                    server.putString("Wooly.rollerArm", "Close");
                    roller.closeArm();
                }
            }
        } else {
            rollerArmToggle.setReleased(true);
            rollerArmToggle.incrementPresses();
        }
    }



    private void doSmallGrabber() {
        //small arm
        if (operatorController.isSmallGrabberButtonPressed()) {
            if (smallGrabberToggle.isReleased()) {
                smallGrabberToggle.incrementPresses();
                grabber.flipSmall();
                smallGrabberToggle.setReleased(false);
            }
        } else {
            smallGrabberToggle.setReleased(true);
        }
    }

    
    

    private void doLargeGrabber() {
        //large arm
        if (operatorController.isLargeGrabberButtonPressed()) {
            if (largeGrabberToggle.isReleased()) {
                largeGrabberToggle.incrementPresses();
                largeGrabberToggle.buttonReleased = false;
                grabber.flipLarge();
            }
        } else {
            largeGrabberToggle.buttonReleased = true;
        }
    }

    public boolean isFireButtonPressed() {
        return operatorController.isFireButtonPressed();
    }
    
    public void update() {
        doLargeGrabber();
        doSmallGrabber();
        doRollerArm();
        doRollerForward();
        doRollerReverse();
        grabber.update();
        firingSolenoid.update();
        rollerSolenoid.update();
        shooter.update();
        screwDrive.update();
        
    }
    
    
    class Toggle {
        private int numberOfPresses = 0;
        private boolean currentState = false;
        private String name;
        private boolean buttonReleased;
        
        public Toggle(String name) {
            this.name = name;
        }
        
        public boolean isOddNumberOfPresses() {
            return numberOfPresses%2 != 0;
        }
        
        public void setCurrentState(boolean state) {
            this.currentState = state;
        }
        
        public void setReleased(boolean released) {
            this.buttonReleased = released;
        }
        
        public boolean isReleased() {
            return buttonReleased;
        }
        
        public String getName() {
            return name;
        }
        
        public int getNumberOfPresses() {
            return numberOfPresses;
        }

        private void incrementPresses() {
            numberOfPresses++;
        }
        
    }
    
}
