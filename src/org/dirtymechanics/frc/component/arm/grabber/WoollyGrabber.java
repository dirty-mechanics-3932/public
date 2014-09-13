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
 * Implements Woolly's grabber mechanism (i.e. the side arms).
 * @author Zach Sussman
 */
public class WoollyGrabber implements Grabber, Updatable{
    /**
    Small solenoids extend arms out to slightly open
    */
    private final DoubleSolenoid smallSolenoids;
    /**
     * Large solenoids open arms out to fully open position
     */
    private final DoubleSolenoid largeSolenoids;
   
    
    public static final int SMALL_MODULE = 1;
    public static final int SMALL_CLOSE_PORT = 1;
    public static final int SMALL_OPEN_PORT = 2;
    
    public static final int LARGE_MODULE = 1;
    public static final int LARGE_CLOSE_PORT = 5;
    public static final int LARGE_OPEN_PORT = 6;
    
    public WoollyGrabber(DoubleSolenoid small, DoubleSolenoid large){
        smallSolenoids = small;
        largeSolenoids = large;
    }

    public void openSmall() {
        smallSolenoids.open();
    }

    public void closeSmall() {
        smallSolenoids.close();
    }
    
    public void flipSmall() {
        smallSolenoids.flip();
    }

    public void openLarge() {
        largeSolenoids.open();
    }

    public void closeLarge() {
        largeSolenoids.close();
    }
    
    public void flipLarge() {
        largeSolenoids.flip();
    }
    
    public WoollyGrabber(){
        Solenoid grabSmallOpen = new Solenoid(1, 1);
        Solenoid grabSmallClose = new Solenoid(1, 2);
        DoubleSolenoid grabSmallSolenoid = new DoubleSolenoid(grabSmallOpen, grabSmallClose);
        smallSolenoids = grabSmallSolenoid;
        
        Solenoid grabLargeOpen = new Solenoid(1, 5);
        Solenoid grabLargeClose = new Solenoid(1, 6);
        DoubleSolenoid grabLargeSolenoid = new DoubleSolenoid(grabLargeOpen, grabLargeClose);
        largeSolenoids = grabLargeSolenoid;
        
    }

    public void update() {
        largeSolenoids.update();
        smallSolenoids.update();
    }
    
}
