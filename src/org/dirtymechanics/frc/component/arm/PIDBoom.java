package org.dirtymechanics.frc.component.arm;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import org.dirtymechanics.frc.sensor.RotationalEncoder;

/**
 *
 * @author Daniel Ruess
 */
public class PIDBoom{
    static final double P = .018d;
    static double D = .009; // The boom, she wants the D
   PIDSubsystem pid;
   public static Location PID_PASS = new Location(500);//GROUND;//new Location(3.26);
   public static final Location PID_ARM_UP_LIMIT = new Location(150);//GROUND;//new Location(3.26);
   public static final Location PID_ARM_DOWN_LIMIT = new Location(780);//GROUND;//new Location(3.26);
   public static final Location MAX = new Location(200);
   public static final Location MIN = new Location(785);

   public static final Location REST = new Location(247);
   public static final Location AUTONOMOUS_SHOT = new Location(336);
   public static final Location HIGH_GOAL = new Location(336);
   public static final Location GROUND = MIN;
   public static final Location PASS = new Location(535);

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
            super("Boom", P, 0, D);
            enable();
        }

        protected double returnPIDInput() {
           
           return rot.pidGet();
        }

       protected void usePIDOutput(double output) {
           motor.set(output);
           System.out.println("motor.set(" + output + ") rot.pidGet()=" + rot.pidGet() + " rot.get()=" + rot.getVoltage() + " getSetpoint()=" + getSetpoint() + " getPostion()=" + getPosition());

       }

       protected void initDefaultCommand() {
       }
    }

    public final void set(Location dest) {
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
        pid.setSetpoint(pid.getSetpoint() - 10);
    }

    public void decreaseOffset() {
        pid.setSetpoint(pid.getSetpoint() + 10);
    }

}