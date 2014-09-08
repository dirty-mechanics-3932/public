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
public class WoollyGrabber extends Grabber implements Updatable{
    private final GrabberArmPair smallArm;
    private final GrabberArmPair largeArm;
   
    
    public static final int SMALL_MODULE = 1;
    public static final int SMALL_CLOSE_PORT = 1;
    public static final int SMALL_OPEN_PORT = 2;
    
    public static final int LARGE_MODULE = 1;
    public static final int LARGE_CLOSE_PORT = 5;
    public static final int LARGE_OPEN_PORT = 6;
    
    public WoollyGrabber(GrabberArmPair small, GrabberArmPair large){
        smallArm = small;
        largeArm = large;
    }

    public void openSmall() {
        smallArm.open();
    }

    public void closeSmall() {
        smallArm.close();
    }
    
    public void flipSmall() {
        smallArm.flip();
    }

    public void openLarge() {
        largeArm.open();
    }

    public void closeLarge() {
        largeArm.close();
    }
    
    public void flipLarge() {
        largeArm.flip();
    }
    
    public static Grabber getGrabber(){
        Solenoid grabSmallOpen = new Solenoid(1, 1);
        Solenoid grabSmallClose = new Solenoid(1, 2);
        DoubleSolenoid grabSmallSolenoid = new DoubleSolenoid(grabSmallOpen, grabSmallClose);
        GrabberArmPair smallArm = new GrabberArmPair(grabSmallSolenoid);
        
        Solenoid grabLargeOpen = new Solenoid(1, 5);
        Solenoid grabLargeClose = new Solenoid(1, 6);
        DoubleSolenoid grabLargeSolenoid = new DoubleSolenoid(grabLargeOpen, grabLargeClose);
        GrabberArmPair largeArm = new GrabberArmPair(grabLargeSolenoid);
        
        return new WoollyGrabber(smallArm, largeArm);
        
    }

    public void update() {
        largeArm.update();
        smallArm.update();
    }
    
}
