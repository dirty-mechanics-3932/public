/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dirtymechanics.frc.component.arm.grabber;

import edu.wpi.first.wpilibj.Solenoid;
import org.dirtymechanics.frc.actuator.DoubleSolenoid;
import org.dirtymechanics.frc.control.OperatorGameController;
import org.dirtymechanics.frc.util.Updatable;

/**
 *
 * @author frc
 */
public class SiblingGrabber extends WoollyGrabber implements Grabber, Updatable{
    /**
     * grabberSolenoids opens the arms
     */
//    private Solenoid grabOpen = new Solenoid(1, 1);
//    private Solenoid grabClose = new Solenoid(1, 2);
//    private DoubleSolenoid grabberSolenoids = new DoubleSolenoid(grabOpen, grabClose);
    
    public SiblingGrabber(OperatorGameController gameController) {
        super(gameController);
        
        // Sorry about the utter lack of principle here
        largeSolenoids = smallSolenoids;
        
    }
    

    
}
