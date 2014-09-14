/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dirtymechanics.frc.component.arm.grabber;

import edu.wpi.first.wpilibj.Solenoid;
import org.dirtymechanics.event.impl.ButtonListener;
import org.dirtymechanics.frc.actuator.DoubleSolenoid;
import org.dirtymechanics.frc.component.arm.event.GrabberLargeButtonEventHandler;
import org.dirtymechanics.frc.component.arm.event.GrabberSmallButtonEventHandler;
import org.dirtymechanics.frc.control.OperatorGameController;
import org.dirtymechanics.frc.util.Updatable;

/**
 * Implements Woolly's grabber mechanism (i.e. the side arms).
 * @author Zach Sussman
 */
public class WoollyGrabber implements Grabber, Updatable{
    
    public static final int RIO_MODULE_FOR_SMALL = 1;
    public static final int RIO_PORT_FOR_SMALL_CLOSE = 1;
    public static final int RIO_PORT_FOR_SMALL_OPEN = 2;
    
    public static final int RIO_MODULE_FOR_LARGE = 1;
    public static final int RIO_PORT_FOR_LARGE_CLOSE = 5;
    public static final int RIO_PORT_FOR_LARGE_OPEN = 6;
    
    private Solenoid grabSmallOpen = new Solenoid(1, 1);
    private Solenoid grabSmallClose = new Solenoid(1, 2);
    
    /**
    Small solenoids extend arms out to slightly open
    */
    private DoubleSolenoid smallSolenoids = new DoubleSolenoid(grabSmallOpen, grabSmallClose);
    
    Solenoid grabLargeOpen = new Solenoid(1, 5);
    Solenoid grabLargeClose = new Solenoid(1, 6);
    DoubleSolenoid largeSolenoids = new DoubleSolenoid(grabLargeOpen, grabLargeClose);
   
    //Shared with sibling
    OperatorGameController gameController;
    GrabberLargeButtonEventHandler largeButtonEventHandler;
    ButtonListener largeButtonListener = new ButtonListener();
    GrabberSmallButtonEventHandler smallButtonEventHandler;
    ButtonListener smallButtonListener = new ButtonListener();
        
    
    public WoollyGrabber(OperatorGameController gameController) {
        this.gameController = gameController;
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

    public boolean isOpenLarge() {
        return largeSolenoids.isOpen();
    }
    
    public void update() {
    }
    
}
