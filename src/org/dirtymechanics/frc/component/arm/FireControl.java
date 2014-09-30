/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dirtymechanics.frc.component.arm;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import org.dirtymechanics.event.impl.ButtonListener;
import org.dirtymechanics.frc.component.arm.event.FireButtonEventHandler;
import org.dirtymechanics.frc.control.OperatorGameController;
import org.dirtymechanics.frc.util.Updatable;

/**
 *
 * @author frc
 */
public class FireControl implements Updatable {
    private long currentTimeMillis = System.currentTimeMillis();
    public static final int FIRE_DELAY = 250;
    NetworkTable server = NetworkTable.getTable("SmartDashboard");
    private boolean fired = false;
    public boolean firing = false;
    private long fireButtonPressTime;
    public long actualFireTime;
    private OperatorGameController operatorGameController;
    private BallManipulator ballManipulator;
    FireButtonEventHandler fireButtonHandler;
    ButtonListener fireButtonListener = new ButtonListener();
//    public ButtonListener fireButtonListener;
    
    
    public FireControl(OperatorGameController operatorGameController, BallManipulator ballManipulator) {
        this.operatorGameController = operatorGameController;
        this.ballManipulator = ballManipulator;
        fireButtonHandler = new FireButtonEventHandler(operatorGameController, ballManipulator);
        fireButtonListener.addHandler(fireButtonHandler);
    }
    
    

    /**
     * called by teleopPeriodic checks
     * the timing state for firing if we're firing
     */
    public void update() {
        currentTimeMillis = System.currentTimeMillis();
        fireButtonListener.updateState(isFireButtonPressed(), currentTimeMillis);
        if (firing) {
            if (isTimeToFire()) {
                debug("fire");
                //insure that we only call fire once...
                if (fired == false) {
                    fire();
                    fired = true;
                }
                //Keep this in here so we only reset controls after firing...
                if (isTimeToResetFireControls()) {
                    System.out.println("resetting fire controls");
                    debug("reset");
                    resetFireControls();
                }
            } 
            
        }
        writeStatus();
    }
    
    void resetFireControls() {
        debug("resetting");
//        disableToggles();
        firing = false;
        fired = false;
        ballManipulator.resetScrewDrive();
        
    }
    
    private boolean isTimeToResetFireControls() {
        final boolean resetDelayExpired = currentTimeMillis - actualFireTime > FIRE_DELAY + 100;
        boolean timeToResetFireControls = resetDelayExpired && fired;
        
        return timeToResetFireControls;
    }

    private boolean isTimeToFire() {
        boolean fireDelayExpired = isFireDelayExpired();
        boolean rangeIsCorrect = true; //isCorrectRange();
        boolean timeToFire = fireDelayExpired && rangeIsCorrect;
        return timeToFire;
    }
    
    public void startFiringSequence(long time) {
       fireButtonPressTime = time;
       debug("prepare to fire");
       firing = true;
       ballManipulator.openRollerArm();
       ballManipulator.openSmall();
    }
    
    public void fire() {
        ballManipulator.openFire();
        actualFireTime = currentTimeMillis;        
    }
    
   
    
    private boolean isFireDelayExpired() {
        long fireDelay = currentTimeMillis - fireButtonPressTime;
        server.putNumber("FireControl.currentFireDelay", fireDelay);
        //TODO put this in init
        server.putNumber("FireControl.FIRE_DELAY", FIRE_DELAY);
        return fireDelay > FIRE_DELAY;
    }
    

    
    public boolean isFireButtonPressed() {
        return operatorGameController.isFireButtonPressed();
    }
    
    boolean firingButtonTimerExpired() {
        return currentTimeMillis - fireButtonPressTime > 500;
    }
    
    
    public void shootAutonomous(long time) {
        if (!firing) {
            ballManipulator.openRollerArm();
            ballManipulator.openSmall();
            firing = true;
            fireButtonPressTime = currentTimeMillis;
        }
        if ((time > 6300 || currentTimeMillis - fireButtonPressTime > 300)) {
            ballManipulator.openFire();
            fired = true;
        }
    }
    
    private boolean debugEnabled = true;
    public void debug(String debugString) {
        if (debugEnabled) {
            server.putString("FireControl.debug", debugString);
        }
    }

    private void writeStatus() {
        server.putBoolean("FiringSequence.isTimeToReset", isTimeToResetFireControls());
        server.putBoolean("FiringSequence.isTimeToFire", isTimeToFire());
        server.putNumber("FiringSequence.startTime", fireButtonPressTime);
        server.putNumber("FiringSequence.fireTime", actualFireTime);
        server.putBoolean("FiringSequence.firing", firing);
        server.putBoolean("FiringSequence.fired", fired);
        server.putNumber("FiringSequence.waitTime", currentTimeMillis - fireButtonPressTime);
        
    }
}

