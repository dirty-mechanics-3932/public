package org.dirtymechanics.frc;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.dirtymechanics.event.ButtonListener;
import org.dirtymechanics.frc.actuator.DoubleSolenoid;
import org.dirtymechanics.frc.component.arm.BoomProperties;
import org.dirtymechanics.frc.component.arm.CompetitionBoomProps;
import org.dirtymechanics.frc.component.arm.Shooter;
import org.dirtymechanics.frc.component.arm.PIDBoom;
import org.dirtymechanics.frc.component.arm.Roller;
import org.dirtymechanics.frc.component.arm.ScrewDrive;
import org.dirtymechanics.frc.component.arm.SiblingBoomProps;
import org.dirtymechanics.frc.component.arm.grabber.Grabber;
import org.dirtymechanics.frc.component.arm.grabber.WoollyGrabber;
import org.dirtymechanics.frc.component.drive.DriveTrain;
import org.dirtymechanics.frc.component.drive.Transmission;
import org.dirtymechanics.frc.control.ButtonMap;
import org.dirtymechanics.frc.sensor.MaxBotixMaxSonarEZ4;
import org.dirtymechanics.frc.sensor.PIDDistanceDrive;
import org.dirtymechanics.frc.sensor.RotationalEncoder;
import org.dirtymechanics.frc.sensor.StringEncoder;
import org.dirtymechanics.frc.util.List;
import org.dirtymechanics.frc.util.Updatable;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Woolly extends IterativeRobot {

    /**
     * The physical left joystick.
     */
    private final Joystick driverLeftJoy;
    /**
     * The physical right joystick.
     */
    private final Joystick driverRightJoy;
    /**
     * The operator's controller.
     */
    final Joystick operatorController;
    /**
     * The operator's joystick.
     */
    private final Joystick operatorJoy;
    /**
     * The compressor's controller.
     */
    private final Compressor compressor;
    /**
     * Jaguar that's driving the first left motor.
     */
    private final Jaguar leftDriveMotorA;
    /**
     * Jaguar that's driving the second left motor.
     */
    private final Jaguar leftDriveMotorB;
    /**
     * Jaguar that's driving the first right motor.
     */
    private final Jaguar rightDriveMotorA;
    /**
     * Jaguar that's driving the second right motor.
     */
    private final Jaguar rightDriveMotorB;
    /**
     * Jaguar controlling the screw drive.
     */
    private final Jaguar screwMotor;
    /**
     * Jaguar controlling the boom.
     */
    private final Talon boomMotor;
    /**
     * Jaguar controller the grabber's roller.
     */
    private final Relay rollerMotor;
    /**
     * The spike controlling the transmission open valve.
     */
    private final Solenoid transOpen;
    /**
     * The spike controlling the transmission close valve.
     */
    private final Solenoid transClose;
    /**
     * The solenoid to switch the transmission.
     */
    private final DoubleSolenoid transmissionSolenoid;
    /**
     * The string encoder used for the screw drive.
     */
    private final StringEncoder stringEncoder;
    /**
     * The rotational encoder used for the boom.
     */
    private final RotationalEncoder rotEncoder;
    final MaxBotixMaxSonarEZ4 ultrasonicSensor;

    private final DigitalInput octo;
    /**
     * The button map used for controllers.
     */
    private final ButtonMap buttonMap;
    /**
     * The object controlling the drive train.
     */
    private final DriveTrain driveTrain;
    /**
     * List of all the updatable objects.
     */
    private final List updatables;
    final Roller roller;
    private final Solenoid firingOpen;
    private final Solenoid firingClose;
    private final DoubleSolenoid firingSolenoid;
//    final GrabberArmPair smallGrabber;
//    final GrabberArmPair largeGrabber;
    private final ScrewDrive screwDrive;
    final Shooter shooter;
    final PIDBoom boom;
//    private final Solenoid grabLargeOpen;
//    private final Solenoid grabLargeClose;
//    final DoubleSolenoid grabLargeSolenoid;
    private final Solenoid rollerOpen;
    private final Solenoid rollerClose;
    private final Solenoid cameraLEDA;
    private final Solenoid cameraLEDB;
    private final Solenoid signalLEDA;
    private final Solenoid signalLEDB;
    private final DoubleSolenoid rollerSolenoid;
    private final Transmission transmission;
    boolean firing;
    private long fireButtonPressTime;
    long actualFireTime;
    private long octoTime;
    private boolean octoSwitchOpen;
    NetworkTable server = NetworkTable.getTable("SmartDashboard");
    double imageMatchConfidence = 0.0;
    private PIDDistanceDrive pidDistanceDrive;

    private final int[] toggle = new int[30];
    private final boolean[] released = new boolean[30];

    private long counter = 0;
    private long autoStart;

    private static final int TRUSS_SHOT_BUTTON = 4;
    boolean fired;
    String firingStatus = "";
    FireButtonEventHandler fireButtonHandler;
    ButtonListener fireButtonListener;
    private int FIRE_BUTTON = 6;

    private final int largeGrabberToggle = 8;
    private final int smallGrabberToggle = 7;
    private final int rollerArmToggle = 5;
    private final int rollerForwardToggle = 10;
    private final int rollerReverseToggle = 9;
    private int idealMaxAutoRange = 112;
    private int idealMinAutoRange = 104;
    private final int MAX_AUTO_RANGE = idealMaxAutoRange;
    private final int MIN_AUTO_RANGE = idealMinAutoRange;
    private int shotsFired = 0;
    private SendableChooser robotPicker;
    final Grabber grabber;

    public Woolly() {
        driverLeftJoy = new Joystick(1);
        driverRightJoy = new Joystick(2);
        operatorController = new Joystick(3);
        operatorJoy = new Joystick(4);

        buttonMap = new ButtonMap(driverLeftJoy, driverRightJoy, operatorController);

        compressor = new Compressor(1, 1);

        leftDriveMotorA = new Jaguar(1);
        leftDriveMotorB = new Jaguar(2);
        rightDriveMotorB = new Jaguar(3);
        rightDriveMotorA = new Jaguar(4);
        boomMotor = new Talon(5);
        screwMotor = new Jaguar(6);
        rollerMotor = new Relay(2);

        transOpen = new Solenoid(1, 7);
        transClose = new Solenoid(1, 8);
        transmissionSolenoid = new DoubleSolenoid(transOpen, transClose);

        grabber = WoollyGrabber.getGrabber();

        firingOpen = new Solenoid(2, 1);
        firingClose = new Solenoid(2, 2);
        firingSolenoid = new DoubleSolenoid(firingOpen, firingClose);

        rollerOpen = new Solenoid(1, 3);
        rollerClose = new Solenoid(1, 4);
        rollerSolenoid = new DoubleSolenoid(rollerOpen, rollerClose);

        cameraLEDA = new Solenoid(2, 7);
        cameraLEDB = new Solenoid(2, 8);
        signalLEDA = new Solenoid(2, 5);
        signalLEDB = new Solenoid(2, 6);

        stringEncoder = new StringEncoder(1);
        rotEncoder = new RotationalEncoder(2);
        ultrasonicSensor = new MaxBotixMaxSonarEZ4(3);
        octo = new DigitalInput(2);

        driveTrain = new DriveTrain(leftDriveMotorA, leftDriveMotorB, rightDriveMotorA, rightDriveMotorB);
        transmission = new Transmission(transmissionSolenoid);
        roller = new Roller(rollerMotor, rollerSolenoid);
        screwDrive = new ScrewDrive(screwMotor, stringEncoder);
        shooter = new Shooter(screwDrive, firingSolenoid);
        boom = new PIDBoom(boomMotor, rotEncoder);
        

//        fireButtonListener = new ButtonListener();
//        fireButtonHandler = new FireButtonEventHandler(operatorController, this);
//        fireButtonListener.addListener(fireButtonHandler);
        updatables = new List();
        updatables.put(transmissionSolenoid);
        updatables.put(grabber);
        updatables.put(firingSolenoid);
        updatables.put(rollerSolenoid);
        updatables.put(shooter);
        //updatables.put(boom);
        updatables.put(screwDrive);
        updatables.put(driveTrain);
        
    }

    /**
     * Called per first initialization of the robot.
     */
    public void robotInit() {
        //See if we can send some better telemetry back
        LiveWindow.addSensor("Boom",  "Rotational Encoder", rotEncoder);
        LiveWindow.addSensor("Boom", "String Encoder", stringEncoder);
        LiveWindow.addSensor("Drive", "Ultrasonic", ultrasonicSensor);
        LiveWindow.addSensor("Boom", "Octo Safety", octo);
        compressor.start();
        fireButtonListener = new ButtonListener();
        fireButtonHandler = new FireButtonEventHandler(operatorController, this);
        fireButtonListener.addListener(fireButtonHandler);
        server.putNumber("idealMaxRange", idealMaxAutoRange);
        server.putNumber("idealMinRange", idealMinAutoRange);
        server.putNumber("BOOM.ROT.PID.IN", 0d);
//        if (boom.BOOM_ENABLED) {
//            boom.set(Boom.HIGH_GOAL);
//        }
        //No point in checking boom enabled here because it will try to go
        //to position "0" by default.
        boom.set(boom.getBoomProperties().getGround());
        
        

        robotPicker = new SendableChooser();
//        robotPicker.addDefault("Competition Robot (Default)", new CompetitionBoomProps());
//        robotPicker.addObject("Sibling Robot", new SiblingBoomProps());
        
        // Since we're going to be configuring a lot based on this, we
        // need a general enumerable.
        
        robotPicker.addDefault("Competition Robot (Default)", RobotType.WOOLLY);
        robotPicker.addObject("Sibling Robot", RobotType.SIBLING);
        
        SmartDashboard.putData("Robot Configuration", robotPicker);
    }
    
    void updateSettings() {
        RobotType robotType = (RobotType) robotPicker.getSelected();
        
        if (robotType == RobotType.WOOLLY){
            boom.setBoomProperties(new CompetitionBoomProps());
        }
        else if (robotType == RobotType.SIBLING){
            boom.setBoomProperties(new SiblingBoomProps());
        }
    }

    public void autonomousInit() {
        updateSettings();
        idealMaxAutoRange = getIntFromServerValue("idealMaxRange", MAX_AUTO_RANGE);
        idealMinAutoRange = getIntFromServerValue("idealMinRange", MIN_AUTO_RANGE);
        autoStart = System.currentTimeMillis();
        disableToggles();
        screwDrive.set(ScrewDrive.AUTONOMOUS_SHOT);
        boom.set(boom.getBoomProperties().getMax());
        transmissionSolenoid.set(true);
        cameraLEDA.set(true);
        cameraLEDB.set(true);
    }
    private boolean hot = false;

    /**
     * This function is called periodically during autonomous.
     */
    public void autonomousPeriodic() {
        long time = System.currentTimeMillis() - autoStart;
        double dist = ultrasonicSensor.getRangeInInches();

        imageMatchConfidence = server.getNumber("HOT_CONFIDENCE", 0.0);

        if (octo.get() && time < 3000) {
            rollerMotor.set(Relay.Value.kForward);
        } else {
            rollerMotor.set(Relay.Value.kOff);
        }

        if (time < 4200) {
            if (time < 175) {
                roller.openArm();
//                grabSmallSolenoid.setOpen();
                grabber.openSmall();
            } else {
                roller.closeArm();
//                grabSmallSolenoid.setClosed();
                grabber.closeSmall();
                boom.set(boom.getBoomProperties().getAutonomousShot());
            }
            if (imageMatchConfidence > 35 && time > 300) {
                hot = true;
            }
            if (dist > 150) {
                driveTrain.setSpeed(.75, .80); //.43
                server.putString("Auto", "Driving");
            } else if (dist > 92) {
                driveTrain.setSpeed(.3, .3); //.43
                server.putString("Auto", "Slowing");
            } else if (dist > 75 && dist < 85) {
                driveTrain.setSpeed(0, 0);
                server.putString("Auto", "Stopped at range");
            } else if (dist < 75) {
                driveTrain.setSpeed(-.3, -.3);
                server.putString("Auto", "Overshot");
            } else {
                driveTrain.setSpeed(0, 0);
                server.putString("Auto", "Stopped");
            }
        } else {
            driveTrain.setSpeed(0, 0);
        }
        

        if (time > 4400) {
            if (hot || imageMatchConfidence > 35 || time > 6000) {
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
        }
        update();
        if (dist < 85) {
            signalLEDA.set(true);
        } else {
            signalLEDA.set(false);
        }
        if (dist > 75) {
            signalLEDB.set(true);
        } else {
            signalLEDB.set(false);
        }
        printDebug();
    }

    void driveForwardUntil3rdSecondOfAutonomous() {
        long time = System.currentTimeMillis() - autoStart;
        if (time < 3000) {
            driveTrain.setSpeed(-.73, .75);
        } else {
            driveTrain.setSpeed(0, 0);
        }

    }

    //Experimental, not currently called.
    void autonomousExperimentalHotGoalShot() {
        //HOT_CONFIDENCE = server.getNumber("HOT_Confidence", 0.0);
        // Algorithm one: One ball, hot vision
        /*
         if (HOT_CONFIDENCE > 75.0) {
         //shoot
         }
         else {
         //wait a few seconds
         }
         */
        //Algorithm two
        long dif = System.currentTimeMillis() - autoStart;
        if (dif > 0 && dif < 2000) {
            driveTrain.setSpeed(-1, 1);
        } else {
            //if (dif > 2000 && dif < 2300) {
            driveTrain.setSpeed(0, 0);
        }/*
         if (dif > 2300) {
         roller.openArm();
         }
         if (dif > 2500) {
         shooter.fire();
         }
         update();*/

    }

    //TODO extract class PeriodicAutonomousShooter, this method would be "update"
    //  members would be appropriate controls passed in constructor.
    //Experimental, not currently called.
    void autonomousExperimintalShooter() {
        /*if (time < 750) {
         boom.set(Boom.AUTO);
         screwDrive.set(ScrewDrive.AUTO);
         roller.forward();
         } else if (octo.get() && time < 1000) {
         roller.forward();
         } else {
         roller.stop();
         }

         if (time < 3500) {
         driveTrain.setSpeed(-.73, .75);
         } else if (time > 3700) {
         grabLargeSolenoid.set(false);
         grabSmallSolenoid.set(false);
         roller.openArm();
         roller.stop();
         if (time > 4200) {
         resetFireControls();
         } else if (time > 4050) {
         shooter.fire();
         }
         } else {

         driveTrain.setSpeed(0, 0);
         }
         update();*/

        long time = System.currentTimeMillis() - autoStart;

        imageMatchConfidence = server.getNumber("HOT_CONFIDENCE", 0.0);
        if (time < 3000) {
            firing = false;
            transmissionSolenoid.set(false);
            screwDrive.set(ScrewDrive.TRUSS_SHOT);
            //boom.set(Boom.AUTONOMOUS_SHOT);
            driveTrain.setSpeed(.43, .5);
        } else {
            driveTrain.setSpeed(0, 0);
            if (imageMatchConfidence > 80 || time > 6000) {
                if (!firing) {
                    roller.openArm();
                    firing = true;
                    fireButtonPressTime = System.currentTimeMillis();
                }
                if (time > 6300 || System.currentTimeMillis() - fireButtonPressTime > 300) {
                    //shooter.fire();
                    firing = false;
                }
            }
        }
        update();
    }

    long t = 0;

    /**
     * This function is called periodically during operator control.
     */
    public void teleopPeriodic() {
        fireButtonListener.updateState(operatorController.getRawButton(FIRE_BUTTON), System.currentTimeMillis());
        if (counter++ % 20 == 0) { //call per 20 cycles
            t = System.currentTimeMillis();
            printDebug();
            turnOffLEDs();
        }

        driveTrain.setSpeed(buttonMap.getDriveLeft(), buttonMap.getDriveRight());
        setTransmission(buttonMap.isTransmissionHigh());
        updateOcto();

        if (firing) {
            if (isArmingRange()) {
                roller.openArm();
//                grabSmallSolenoid.setOpen();
                grabber.openSmall();
                roller.stop();
            }
            if (isTimeToResetFireControls()) {
                fireButtonHandler.resetFireControls();
            } else if (isFireDelayExpired()) {
                fireButtonHandler.fire(isCorrectRange());

            } else {
                fireButtonHandler.prepareToFire();
            }
//            if (!isFireButtonPressed()) {
//                resetFireControls();
//            }
        }

        updateScrewDrive();
        updateBoom();
        updateRangeLEDs();

        if (!firing) {
            checkArmControls();

        }

        update();
    }

    private void checkArmControls() {
        checkLargeGrabberButton();
        checkSmallGrabberButton();
        checkRollerArmButton();
        checkRollerForwardButton();
        checkRollerReverseButton();
    }

    private void checkRollerReverseButton() {
        //roller rev
        if (operatorController.getRawButton(9)) {
            if (released[9]) {
                toggle[9]++;
                released[9] = false;
                if (toggle[9]%2 == 0){
                    roller.reverse();
                }
                else {
                    roller.stop();
                }
            }
        } else {
            released[9] = true;
        }
    }

    private void checkRollerForwardButton() {
        //roller forward
        if (operatorController.getRawButton(10)) {
            if (released[10]) {
                toggle[10]++;
                released[10] = false;
                if (toggle[10]%2 == 0) {
                    roller.forward();
                }
                else {
                    roller.stop();
                }
            }
        } else {
            released[10] = true;
        }
    }

    private void checkRollerArmButton() {
        //roller arm
        if (operatorController.getRawButton(5)) {
            if (released[5]) {
                toggle[5]++;
                released[5] = false;
                if (toggle[5]%2 != 0) {
                    roller.openArm();
                }
                else {
                    roller.closeArm();
                }
            }
        } else {
            released[5] = true;
        }
    }

    private void checkSmallGrabberButton() {
        //small arm
        if (operatorController.getRawButton(7)) {
            if (released[7]) {
                toggle[7]++;
//                grabSmallSolenoid.flip();
                grabber.flipSmall();
                released[7] = false;
            }
        } else {
            released[7] = true;
        }
    }

    private void checkLargeGrabberButton() {
        //large arm
        if (operatorController.getRawButton(8)) {
            if (released[8]) {
                toggle[8]++;
                released[8] = false;
//                grabLargeSolenoid.flip();
                grabber.flipLarge();
            }
        } else {
            released[8] = true;
        }
    }

    private boolean isTimeToResetFireControls() {
        final boolean resetDelayExpired = System.currentTimeMillis() - actualFireTime > 250;
        return resetDelayExpired && fired;
    }


    private boolean isFireDelayExpired() {
        return System.currentTimeMillis() - fireButtonPressTime > 350;
    }

    private boolean isArmingRange() {
        return ultrasonicSensor.getRangeInInches() > 100 && ultrasonicSensor.getRangeInInches() < 115;
    }

    private boolean isCorrectRange() {
        return ultrasonicSensor.getRangeInInches() > 75 && ultrasonicSensor.getRangeInInches() < 85;
    }
    
//Range leds use isCorrectRange - don't send mixed messages, but this is the old range...    
//    private boolean isFiringRange() {
//        return ultrasonicSensor.getRangeInInches() > 108 && ultrasonicSensor.getRangeInInches() < 111;
//    }


    boolean isBallSwitchOpen() {
        return !octo.get();
    }

    boolean holdingTheBallAndNotFiring() {
        return !octoSwitchOpen && !firing;
    }

    void updateRangeLEDs() {
        if (isCorrectRange()) {
            rangeLeds(true);
        } else {
            rangeLeds(false);
        }
    }

    void updateBoom() {
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
            setToggle(smallGrabberToggle, true);
            setToggle(rollerForwardToggle, true);
            setToggle(rollerReverseToggle, false);
            
            roller.forward();
//            grabSmallSolenoid.setOpen();
            grabber.openSmall();
        }
    }

    void updateScrewDrive() {
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

    boolean firingButtonTimerExpired() {
        return System.currentTimeMillis() - fireButtonPressTime > 500;
    }

    void updateOcto() {
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
                if (!operatorController.getRawButton(rollerReverseToggle)) {
                    setToggle(rollerReverseToggle, false);
                }
                if (System.currentTimeMillis() - octoTime > 600) {
                    setToggle(rollerArmToggle, false);
                }
                setToggle(largeGrabberToggle, false);
                setToggle(smallGrabberToggle, false);
                setToggle(rollerForwardToggle, false);
                
//                smallGrabber.close();
                grabber.closeSmall();
            }
        }
    }

    void setTransmission(boolean transmissionHigh) {
        if (transmissionHigh) {
            transmission.setHigh();
        } else {
            transmission.setLow();
        }
    }

    void turnOffLEDs() {
        cameraLEDA.set(false);
        cameraLEDB.set(false);
    }

    void printDebug() {
        
        server.putNumber("BOOM.ROT", rotEncoder.getAverageVoltage());
        server.putNumber("BOOM.ROT.PID", rotEncoder.pidGet());
        server.putNumber("BOOM.LIN", stringEncoder.getAverageVoltage());
        server.putNumber("BOOM.RANGE.V", ultrasonicSensor.getAverageVoltage());
        server.putNumber("IMAGE.CONF", imageMatchConfidence);
        server.putNumber("BOOM.RANGE.I", ultrasonicSensor.getRangeInInches());
        server.putBoolean("OCT", octo.get());
        //System.out.println("firingStatus=" + firingStatus + " firing=" + firing + " fired=" + fired + " fireButtonPressTime=" + fireButtonPressTime + " actualFireTime=" + actualFireTime);
        //System.out.println("isTimeToResetFireControls=" + isTimeToResetFireControls() + " fireDelayExpired=" + isFireDelayExpired() + " isCorrectRange=" + isCorrectRange());
    }

    void setToggle(int num, boolean value) {
        if (toggle[num] % 2 == 0 != value) {
            toggle[num]++;
        }
    }

    void disableToggles() {
        for (int i = 0; i < toggle.length; ++i) {
            setToggle(i, false);
        }
    }

    /**
     * This function is used to update all the updatable objects.
     */
    private void update() {
        Object[] o = updatables.getObjects();
        for (int i = 0; i < o.length; ++i) {
            Updatable ud = (Updatable) o[i];
            ud.update();
        }
    }

    public void teleopInit() {
        updateSettings();
        cameraLEDA.set(false);
        cameraLEDB.set(false);
        signalLEDA.set(false);
        signalLEDB.set(false);
        //screwDrive.set(ScrewDrive.RESET);
        disableToggles();
    }

    public void rangeLeds(boolean b) {
        signalLEDA.set(b);
        signalLEDB.set(b);
    }

    private void logFiringTelemetry() {
        final String logMessage = System.currentTimeMillis() + " Range="
                + ultrasonicSensor.getRangeInInches() + ", Rot.v=" + rotEncoder.getAverageVoltage() + ", Str.volt="
                + stringEncoder.getAverageVoltage() + ", leftA=" + leftDriveMotorA.getSpeed()
                + ", rightA=" + rightDriveMotorA.getSpeed();
        System.out.println(logMessage);
        server.putString(++shotsFired + "shot", logMessage);
    }

    int getIntFromServerValue(String tableKey, int defaultValue) {
        try {
            return (int) server.getNumber(tableKey);
        } catch (Throwable e) {
            System.out.println("failed to get tableKey=" + tableKey);
            return defaultValue;
        }
    }


    
    public void testPeriodic(){
       
    }
}
