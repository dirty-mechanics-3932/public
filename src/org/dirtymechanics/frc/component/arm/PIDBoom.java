package org.dirtymechanics.frc.component.arm;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import org.dirtymechanics.event.impl.ButtonListener;
import org.dirtymechanics.frc.component.arm.event.BoomDecreaseOffsetButtonEventHandler;
import org.dirtymechanics.frc.component.arm.event.BoomGroundButtonEventHandler;
import org.dirtymechanics.frc.component.arm.event.BoomHighButtonEventHandler;
import org.dirtymechanics.frc.component.arm.event.BoomIncreaseOffsetButtonEventHandler;
import org.dirtymechanics.frc.component.arm.event.BoomPassButtonEventHandler;
import org.dirtymechanics.frc.component.arm.event.BoomRestButtonEventHandler;
import org.dirtymechanics.frc.control.OperatorGameController;
import org.dirtymechanics.frc.control.OperatorJoystick;
import org.dirtymechanics.frc.sensor.RotationalEncoder;
import org.dirtymechanics.frc.util.Updatable;


public class PIDBoom implements Updatable {
    PIDSubsystem pid;
    private BoomProperties boomProperties = new BoomProps();
    Talon motor;
    RotationalEncoder rot;
    public boolean BOOM_ENABLED = true;
    OperatorJoystick operatorJoy;
    BoomIncreaseOffsetButtonEventHandler increaseOffsetButtonEventHandler;
    ButtonListener increaseOffsetListener = new ButtonListener();
    BoomDecreaseOffsetButtonEventHandler decreaseOffsetButtonEventHandler;
    ButtonListener decreaseOffsetListener = new ButtonListener();
    
    BoomRestButtonEventHandler restButtonEventHandler;
    ButtonListener restButtonListener = new ButtonListener();
    
    BoomHighButtonEventHandler highButtonEventHandler;
    ButtonListener highButtonListener = new ButtonListener();
    
    BoomPassButtonEventHandler passButtonEventHandler;
    ButtonListener passButtonListener = new ButtonListener();
    
    BoomGroundButtonEventHandler groundButtonEventHandler;
    ButtonListener groundButtonListener = new ButtonListener();
    OperatorGameController gameController;
    
    
    
    public PIDBoom(Talon motor, RotationalEncoder rot, OperatorJoystick operatorJoy, OperatorGameController gameController) {
        this.motor = motor;
        this.rot = rot;
        pid = new BoomPIDController();
        this.operatorJoy = operatorJoy;
        this.gameController = gameController;

        increaseOffsetButtonEventHandler = new BoomIncreaseOffsetButtonEventHandler(operatorJoy, this);
        decreaseOffsetButtonEventHandler = new BoomDecreaseOffsetButtonEventHandler(operatorJoy, this);
        restButtonEventHandler = new BoomRestButtonEventHandler(gameController, this);
        highButtonEventHandler = new BoomHighButtonEventHandler(gameController, this);
        passButtonEventHandler = new BoomPassButtonEventHandler(gameController, this);
        groundButtonEventHandler = new BoomGroundButtonEventHandler(gameController, this);
        
    }
    
    public void init() {
        increaseOffsetListener.addHandler(increaseOffsetButtonEventHandler);
        decreaseOffsetListener.addHandler(decreaseOffsetButtonEventHandler);
        restButtonListener.addHandler(restButtonEventHandler);
        highButtonListener.addHandler(highButtonEventHandler);
        passButtonListener.addHandler(passButtonEventHandler);
        groundButtonListener.addHandler(groundButtonEventHandler);
    }

    
     public static class Location {

        public double loc;

        public Location(double loc) {
            this.loc = loc;
        }
    }
    
    class BoomPIDController extends PIDSubsystem {
        
        public BoomPIDController() {
            super("Boom", boomProperties.getP(), 0, boomProperties.getD());
            enable();
        }

        protected double returnPIDInput() {
           return rot.pidGet();
        }

       protected void usePIDOutput(double output) {
           motor.set(output);
       }

       protected void initDefaultCommand() {
       }
    }

    private void set(Location dest) {
       pid.setSetpoint(dest.loc);
    }
    
    public void high() {
        set(getBoomProperties().getHighGoal());
    }
    
    public void pass() {
        set(getBoomProperties().getPass());
    }
    
    public void rest() {
        set(getBoomProperties().getRest());
    }
    
    public void ground() {
        set(getBoomProperties().getGround());
    }
    
    public void autonomous() {
        set(getBoomProperties().getAutonomousShot());
    }
   
    public void update() {
        long currentTime = System.currentTimeMillis();
        //PID will handle updating the positition
        increaseOffsetListener.updateState(operatorJoy.isIncreaseBoomOffsetPressed(), currentTime);
        decreaseOffsetListener.updateState(operatorJoy.isDecreaseBoomOffsetPressed(), currentTime);
        restButtonListener.updateState(gameController.isRestButtonPressed(), currentTime);
        highButtonListener.updateState(gameController.isHighGoalButtonPressed(), currentTime);
        passButtonListener.updateState(gameController.isPassButtonPressed(), currentTime);
        groundButtonListener.updateState(gameController.isGroundButtonPressed(), currentTime);
    }
    
    public void increaseOffset() {
        pid.setSetpoint(pid.getSetpoint() - boomProperties.getMoveIncrementSize());
    }

    public void decreaseOffset() {
        pid.setSetpoint(pid.getSetpoint() + boomProperties.getMoveIncrementSize());
    }
    
    public BoomProperties getBoomProperties() {
        return boomProperties;
    }

    public void setBoomProperties(BoomProperties boomProperties) {
        this.boomProperties = boomProperties;
    }


}