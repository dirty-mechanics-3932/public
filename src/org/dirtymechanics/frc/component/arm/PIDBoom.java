package org.dirtymechanics.frc.component.arm;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import org.dirtymechanics.frc.sensor.RotationalEncoder;

/**
 *
 * @author Daniel Ruess
 */
public class PIDBoom {
    PIDSubsystem pid;
    private BoomProperties boomProperties = new CompetitionBoomProps();
    final Talon motor;
    final RotationalEncoder rot;
    public boolean BOOM_ENABLED = true;
    
    
    public PIDBoom(Talon motor, RotationalEncoder rot) {
    //public PIDBoom(Talon motor, AnalogChannel rot) {
        this.motor = motor;
        this.rot = rot;
        pid = new BoomPIDController();
        
        //Boom does this in it's constructor, but without the pid 
        //  enabled that won't do anything.
//        if (BOOM_ENABLED) {
//            set(PASS);
//        }
        
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
//           System.out.println("motor.set(" + output + ") rot.pidGet()=" + rot.pidGet() + " rot.get()=" + rot.getVoltage() + " getSetpoint()=" + getSetpoint() + " getPostion()=" + getPosition());

       }

       protected void initDefaultCommand() {
       }
    }

    public void set(Location dest) {
       pid.setSetpoint(dest.loc);
//        pid.setSetpoint(PID_PASS.loc);
    }

//    public void increaseOffset() {
//        dest -= .05;
//        pid.setSetpoint(dest);
//    }
//
//    public void decreaseOffset() {
//        dest += .05;
//        pid.setSetpoint(dest);
//    }

    /**
     * overrides parent class method to do nothing.
     */
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