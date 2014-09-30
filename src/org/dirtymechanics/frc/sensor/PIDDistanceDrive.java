/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dirtymechanics.frc.sensor;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import org.dirtymechanics.frc.component.drive.DriveTrain;

/**
 *
 * @author frc
 */
public class PIDDistanceDrive {
    private final double P = .02;
    private final double D = .02;
    private UltrasonicPIDController pid;
    private AnalogChannel rangefinder;
    private DriveTrain driveTrain;
    
    public PIDDistanceDrive(AnalogChannel rangefinder, DriveTrain driveTrain){
        this.rangefinder = rangefinder;
        this.driveTrain = driveTrain;
        pid = new UltrasonicPIDController();
    }
    
   
    
    class UltrasonicPIDController extends PIDSubsystem {
        
        public UltrasonicPIDController() {
            super("Ultrasonic", P, 0, D);
            enable();
        }

        protected double returnPIDInput() {
           return rangefinder.pidGet();
       }

       protected void usePIDOutput(double output) {
           double reverseOutput = -1 * output; // Reverse wheel direction
           driveTrain.setSpeed(reverseOutput, reverseOutput);
       }

       protected void initDefaultCommand() {
       }
    }
    
    public void set(double distance){
        pid.setSetpoint(distance);
    }
}
