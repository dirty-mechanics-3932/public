/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dirtymechanics.frc.component.arm.grabber;

import edu.wpi.first.wpilibj.Solenoid;
import org.dirtymechanics.frc.actuator.DoubleSolenoid;
import org.dirtymechanics.frc.util.Updatable;

/**
 *
 * @author frc
 */
public class SiblingGrabber implements Grabber, Updatable{
    /**
     * grabberSolenoids opens the arms
     */
    private GrabberSolenoidPair grabberSolenoids;
    
    public SiblingGrabber(GrabberSolenoidPair arm){
        grabberSolenoids = arm;
    }
    
    public SiblingGrabber(){
        Solenoid grabOpen = new Solenoid(1, 1);
        Solenoid grabClose = new Solenoid(1, 2);
        DoubleSolenoid grabSolenoid = new DoubleSolenoid(grabOpen, grabClose);
        grabberSolenoids = new GrabberSolenoidPair(grabSolenoid);
    }
    
    public void openSmall(){
        grabberSolenoids.open();
    }
    
    public void closeSmall(){
        grabberSolenoids.close();
    }
    
    public void flipSmall(){
        grabberSolenoids.flip();
    }
    
    public void openLarge(){
        grabberSolenoids.open();
    }
    
    public void closeLarge(){
        grabberSolenoids.close();
    }
    
    public void flipLarge(){
        grabberSolenoids.flip();
    }

    public void update() {
        grabberSolenoids.update();
    }
    
    
    
}
