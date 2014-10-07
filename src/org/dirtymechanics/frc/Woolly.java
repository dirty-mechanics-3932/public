// Woolly

package org.dirtymechanics.frc;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.dirtymechanics.frc.actuator.DoubleSolenoid;
import org.dirtymechanics.frc.component.arm.BallManipulator;
import org.dirtymechanics.frc.component.drive.Transmission;
import org.dirtymechanics.frc.control.DriveControl;
import org.dirtymechanics.frc.control.DriverLeftStick;
import org.dirtymechanics.frc.control.DriverRightStick;
import org.dirtymechanics.frc.sensor.Camera;
import org.dirtymechanics.frc.sensor.CameraButtonEventHandler;
import org.dirtymechanics.frc.sensor.CameraButtonListener;
import org.dirtymechanics.frc.sensor.PIDDistanceDrive;
import org.dirtymechanics.frc.util.List;
import org.dirtymechanics.frc.util.Updatable;

/**
 * The deployed software is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Woolly extends IterativeRobot {
    private final DriverLeftStick left = new DriverLeftStick(DRIVER_LEFT_JOY_USB_PORT);
    public static final int DRIVER_LEFT_JOY_USB_PORT = 1;
    private final DriverRightStick right = new DriverRightStick(DRIVER_RIGHT_JOY_USB_PORT);
    public static final int DRIVER_RIGHT_JOY_USB_PORT = 2;
    public static final int AUTONOMOUS_DRIVE_TIME = 4400;
    public static final int MAX_CONFIDENCE_WAIT = 6000;

    
    private final Compressor compressor = new Compressor(1, 1);
    
 
    /**
     * The spike controlling the transmission open valve.
     */
    private final Solenoid transOpen = new Solenoid(1, 7);
    /**
     * The spike controlling the transmission close valve.
     */
    private final Solenoid transClose = new Solenoid(1, 8);;
    /**
     * The solenoid to switch the transmission.
     */
    private final DoubleSolenoid transmissionSolenoid = new DoubleSolenoid(transOpen, transClose);
    
    
    private final DriveControl driveControl = new DriveControl(left, right);
    
    /**
     * List of all the updatable objects.
     */
    private List updatables = new List();


    
    private final Solenoid cameraLEDA = new Solenoid(2, 7);
    private final Solenoid cameraLEDB = new Solenoid(2, 8);
    private final Solenoid signalLEDA = new Solenoid(2, 5);
    private final Solenoid signalLEDB = new Solenoid(2, 6);

    private final Transmission transmission = new Transmission(transmissionSolenoid);
    NetworkTable server = NetworkTable.getTable("SmartDashboard");
    double imageMatchConfidence = 0.0;
    private PIDDistanceDrive pidDistanceDrive;

    private long counter = 0;
    private long autoStart;

    private int idealMaxAutoRange = 112;
    private int idealMinAutoRange = 104;
    private final int MAX_AUTO_RANGE = idealMaxAutoRange;
    private final int MIN_AUTO_RANGE = idealMinAutoRange;
    private int shotsFired = 0;
    
    private SendableChooser robotPicker;
    
    BallManipulator ballManipulator = new BallManipulator();
    long systemTime = 0;
    Camera camera = new Camera(cameraLEDA ,cameraLEDB);
    CameraButtonListener cameraButtonListner = new CameraButtonListener(left);
    CameraButtonEventHandler cameraButtonHandler = new CameraButtonEventHandler(camera);

    

    public Woolly() {
    }

    /**
     * Called per first initialization of the robot.
     */
    public void robotInit() {
        cameraButtonListner.addHandler(cameraButtonHandler);
        //TODO since we're adding sensors here do we need all the debugging or is it
        //just noise in the dashboard?
        //See if we can send some better telemetry back
        LiveWindow.addSensor("Boom",  "Rotational Encoder", ballManipulator.rotEncoder);
        LiveWindow.addSensor("Drive", "Ultrasonic", ballManipulator.ultrasonicSensor);
        LiveWindow.addSensor("Boom", "Octo Safety", ballManipulator.octo);
        compressor.start();

        robotPicker = new SendableChooser();
        
        robotPicker.addObject("Robot", RobotType.WOOLLY);
//        robotPicker.addObject("Sibling Robot", RobotType.SIBLING);
        
        SmartDashboard.putData("Robot Configuration", robotPicker);
        
        updateSettings();
        ballManipulator.init();
        
        updatables = new List();
//        updatables.put(transmissionSolenoid);
        updatables.put(ballManipulator);
        updatables.put(driveControl);
        updatables.put(cameraButtonListner);
        
        cameraLEDA.set(true);
        cameraLEDB.set(true);
    }
    
    void updateSettings() {
        RobotType robotType = (RobotType) robotPicker.getSelected();
        selectRobot(robotType);        
    }

    private void selectRobot(RobotType robotType) {
        ballManipulator.setType(robotType);
        
    }

    public void autonomousInit() {
        idealMaxAutoRange = getIntFromServerValue("idealMaxRange", MAX_AUTO_RANGE);
        idealMinAutoRange = getIntFromServerValue("idealMinRange", MIN_AUTO_RANGE);
        autoStart = System.currentTimeMillis();
        //TODO make screwdrive private and encapsulate these ina method
        ballManipulator.setScrewDriveAutonomous();
        ballManipulator.setBoomRest();
        
        transmissionSolenoid.open();
        cameraLEDA.set(true);
        cameraLEDB.set(true);
    }
    private boolean hot = false;
    private boolean firedAuto = false;

    /**
     * This function is called periodically during autonomous.
     */
    public void autonomousPeriodic() {
        //TODO the guts of this method should be broken out into a 
        //periodic class that is broken into more managable methods.
        ballManipulator.update();
        long timeElapsedSinceAutoStarted = System.currentTimeMillis() - autoStart;
        double dist = ballManipulator.ultrasonicSensor.getRangeInInches();

        imageMatchConfidence = server.getNumber("HOT_CONFIDENCE", 0.0);
        
        //Limit below-->the higher it is the more specificity needed
        double imageMatchConfidenceLimit = 35;

        if (ballManipulator.octo.get() && timeElapsedSinceAutoStarted < 3000) {
            ballManipulator.rollerForward();
        } else {
            ballManipulator.rollerStop();
        }

        if (timeElapsedSinceAutoStarted < 4200) {
            loadBall(timeElapsedSinceAutoStarted);
            checkForHotGoal(imageMatchConfidenceLimit, timeElapsedSinceAutoStarted);
            driveForward(dist);
        } else {
            driveControl.setRawSpeed(0, 0);
        }
        
//        driveForwardUntil3rdSecondOfAutonomous();

        if (isTimeForAutonomousShot(timeElapsedSinceAutoStarted, imageMatchConfidenceLimit)) {
            System.out.println("Shooting...");
            ballManipulator.shootAutonomous(System.currentTimeMillis());
            
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

    public void driveForward(double dist) {
        if (dist > 100) {
            driveControl.setRawSpeed(.80, .80); //.43
            server.putString("Auto", "Driving");
        } else if (dist > 80) {
            driveControl.setRawSpeed(.4, .3); //.43
            server.putString("Auto", "Slowing");
        } else if (dist > 70 && dist < 80) {
            driveControl.setRawSpeed(0, 0);
            server.putString("Auto", "Stopped at range");
        } else if (dist < 65) {
            driveControl.setRawSpeed(-.3, -.3);
            server.putString("Auto", "Overshot");
        } else {
            driveControl.setRawSpeed(0, 0);
            server.putString("Auto", "Stopped");
        }
    }

    public void checkForHotGoal(double imageMatchConfidenceLimit, long timeElapsedSinceAutoStarted) {
        if (imageMatchConfidence > imageMatchConfidenceLimit && timeElapsedSinceAutoStarted > 300) {
            hot = true;
        }
    }

    public void loadBall(long timeElapsedSinceAutoStarted) {
        if (timeElapsedSinceAutoStarted < 175) {
            ballManipulator.rollerArmOpen();
//                grabSmallSolenoid.setOpen();
            ballManipulator.grabber.openSmall();
        } else {
            ballManipulator.roller.closeArm();
//                grabSmallSolenoid.setClosed();
            ballManipulator.grabber.closeSmall();
            ballManipulator.setBoomAutonomouseShot();
            
        }
    }

    public boolean isTimeForAutonomousShot(long time, double imageMatchConfidenceLimit) {
        return time > MAX_CONFIDENCE_WAIT;
//        return (time > AUTONOMOUS_DRIVE_TIME) && (hot || imageMatchConfidence > imageMatchConfidenceLimit || time > MAX_CONFIDENCE_WAIT);
    }


    

    void driveForwardUntil3rdSecondOfAutonomous() {
        long time = System.currentTimeMillis() - autoStart;
        if (time < 2900) {
            driveControl.setRawSpeed(.73, .8); 
        } else {
            driveControl.setRawSpeed(0, 0);
        }

    }

    

    /**
     * This function is called periodically during operator control.
     */
    public void teleopPeriodic() {
        if (counter++ % 20 == 0) { //call per 20 cycles
            systemTime = System.currentTimeMillis();
            printDebug();
//            turnOffLEDs();
        }
        
        driveControl.setSpeed();
        
        setTransmission(driveControl.isTransmissionHigh()); 
        //TODO isn't this being done by update?  It should be...
        
        updateRangeLEDs();
        update();
    }

    void updateRangeLEDs() {
        if (ballManipulator.isCorrectRange()) {
            rangeLeds(true);
        } else {
            rangeLeds(false);
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
        //TODO why ground?
        ballManipulator.boomGround();
        
        cameraLEDA.set(false);
        cameraLEDB.set(false);
        signalLEDA.set(false);
        signalLEDB.set(false);

    }

    public void rangeLeds(boolean b) {
        signalLEDA.set(b);
        signalLEDB.set(b);
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
