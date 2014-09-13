package org.dirtymechanics.frc.component.arm;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import org.dirtymechanics.frc.sensor.RotationalEncoder;


public class PIDBoom {
    PIDSubsystem pid;
    private BoomProperties boomProperties = new BoomProps();
    final Talon motor;
    final RotationalEncoder rot;
    public boolean BOOM_ENABLED = true;
    
    
    public PIDBoom(Talon motor, RotationalEncoder rot) {
        this.motor = motor;
        this.rot = rot;
        pid = new BoomPIDController();

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

    public void set(Location dest) {
       pid.setSetpoint(dest.loc);
    }


    public void update() {
        //leave updateing the position to the pid
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