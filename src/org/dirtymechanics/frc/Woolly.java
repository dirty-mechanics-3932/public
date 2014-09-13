package org.dirtymechanics.frc;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.dirtymechanics.frc.actuator.DoubleSolenoid;
import org.dirtymechanics.frc.component.arm.BallManipulator;
import org.dirtymechanics.frc.component.arm.ScrewDrive;
import org.dirtymechanics.frc.component.drive.Transmission;
import org.dirtymechanics.frc.control.DriveControl;
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
    
    
    private final DriveControl driveControl = new DriveControl();
    
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
    

    public Woolly() {
    }

    /**
     * Called per first initialization of the robot.
     */
    public void robotInit() {
        //TODO since we're adding sensors here do we need all the debugging or is it
        //just noise in the dashboard?
        //See if we can send some better telemetry back
        LiveWindow.addSensor("Boom",  "Rotational Encoder", ballManipulator.rotEncoder);
        LiveWindow.addSensor("Boom", "String Encoder", ballManipulator.stringEncoder);
        LiveWindow.addSensor("Drive", "Ultrasonic", ballManipulator.ultrasonicSensor);
        LiveWindow.addSensor("Boom", "Octo Safety", ballManipulator.octo);
        compressor.start();
        ballManipulator.init();
        server.putNumber("idealMaxRange", idealMaxAutoRange);
        server.putNumber("idealMinRange", idealMinAutoRange);
        server.putNumber("BOOM.ROT.PID.IN", 0d);

        robotPicker = new SendableChooser();
        
        robotPicker.addDefault("Competition Robot (Default)", RobotType.WOOLLY);
        robotPicker.addObject("Sibling Robot", RobotType.SIBLING);
        
        SmartDashboard.putData("Robot Configuration", robotPicker);
        
        updateSettings();
        
        updatables = new List();
        updatables.put(transmissionSolenoid);
        updatables.put(ballManipulator);
        updatables.put(driveControl);
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
        ballManipulator.screwDrive.set(ScrewDrive.AUTONOMOUS_SHOT);
        ballManipulator.boom.set(ballManipulator.boom.getBoomProperties().getMax());
        transmissionSolenoid.setOpen(true);
        cameraLEDA.set(true);
        cameraLEDB.set(true);
    }
    private boolean hot = false;

    /**
     * This function is called periodically during autonomous.
     */
    public void autonomousPeriodic() {
        long time = System.currentTimeMillis() - autoStart;
        double dist = ballManipulator.ultrasonicSensor.getRangeInInches();

        imageMatchConfidence = server.getNumber("HOT_CONFIDENCE", 0.0);

        if (ballManipulator.octo.get() && time < 3000) {
            ballManipulator.rollerMotor.set(Relay.Value.kForward);
        } else {
            ballManipulator.rollerMotor.set(Relay.Value.kOff);
        }

        if (time < 4200) {
            if (time < 175) {
                ballManipulator.roller.openArm();
//                grabSmallSolenoid.setOpen();
                ballManipulator.grabber.openSmall();
            } else {
                ballManipulator.roller.closeArm();
//                grabSmallSolenoid.setClosed();
                ballManipulator.grabber.closeSmall();
                ballManipulator.boom.set(ballManipulator.boom.getBoomProperties().getAutonomousShot());
            }
            if (imageMatchConfidence > 35 && time > 300) {
                hot = true;
            }
            if (dist > 150) {
                driveControl.setSpeed(.75, .80); //.43
                server.putString("Auto", "Driving");
            } else if (dist > 92) {
                driveControl.setSpeed(.3, .3); //.43
                server.putString("Auto", "Slowing");
            } else if (dist > 75 && dist < 85) {
                driveControl.setSpeed(0, 0);
                server.putString("Auto", "Stopped at range");
            } else if (dist < 75) {
                driveControl.setSpeed(-.3, -.3);
                server.putString("Auto", "Overshot");
            } else {
                driveControl.setSpeed(0, 0);
                server.putString("Auto", "Stopped");
            }
        } else {
            driveControl.setSpeed(0, 0);
        }
        

        if (time > 4400) {
            if (hot || imageMatchConfidence > 35 || time > 6000) {
                ballManipulator.shootAutonomous(time);
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
            driveControl.setSpeed(-.73, .75);
        } else {
            driveControl.setSpeed(0, 0);
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
            driveControl.setSpeed(-1, 1);
        } else {
            //if (dif > 2000 && dif < 2300) {
            driveControl.setSpeed(0, 0);
        }/*
         if (dif > 2300) {
         roller.openArm();
         }
         if (dif > 2500) {
         shooter.fire();
         }
         update();*/

    }


    long t = 0;

    /**
     * This function is called periodically during operator control.
     */
    public void teleopPeriodic() {
        //TODO encapsulate in method
        ballManipulator.fireButtonListener.updateState(ballManipulator.isFireButtonPressed(), System.currentTimeMillis());
        if (counter++ % 20 == 0) { //call per 20 cycles
            t = System.currentTimeMillis();
            printDebug();
            turnOffLEDs();
        }
        
        driveControl.setSpeed();
        
        setTransmission(driveControl.isTransmissionHigh());
        //TODO isn't this being done by update?  It should be...
        ballManipulator.updateOcto();
        ballManipulator.updateScrewDrive();
        ballManipulator.updateBoom();
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
        server.putNumber("BOOM.ROT", ballManipulator.rotEncoder.getAverageVoltage());
        server.putNumber("BOOM.ROT.PID", ballManipulator.rotEncoder.pidGet());
        server.putNumber("BOOM.LIN", ballManipulator.stringEncoder.getAverageVoltage());
        server.putNumber("BOOM.RANGE.V", ballManipulator.ultrasonicSensor.getAverageVoltage());
        server.putNumber("IMAGE.CONF", imageMatchConfidence);
        server.putNumber("BOOM.RANGE.I", ballManipulator.ultrasonicSensor.getRangeInInches());
        server.putBoolean("OCT", ballManipulator.octo.get());
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
        //TODO encapsulate in method
        ballManipulator.boom.set(ballManipulator.boom.getBoomProperties().getGround());
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
