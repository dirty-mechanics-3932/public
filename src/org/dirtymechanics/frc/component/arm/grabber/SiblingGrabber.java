/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dirtymechanics.frc.component.arm.grabber;

/**
 *
 * @author frc
 */
public class SiblingGrabber extends Grabber {
    private GrabberArmPair grabberArm;
    
    public SiblingGrabber(GrabberArmPair arm){
        grabberArm = arm;
    }
    
    public void openSmall(){
        grabberArm.open();
    }
    
    public void closeSmall(){
        grabberArm.close();
    }
    
    public void flipSmall(){
        grabberArm.flip();
    }
    
    public void openLarge(){
        grabberArm.open();
    }
    
    public void closeLarge(){
        grabberArm.close();
    }
    
    public void flipLarge(){
        grabberArm.flip();
    }
    
}
