/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dirtymechanics.frc.control;

/**
 *
 * @author agresh
 */
public class DriverLeftStick extends DriverStick implements BasicJoystick {
    double speed = 0;

    public DriverLeftStick(int port) {
        super(port);
    }
    
    public double updateDriveSpeed() {
        double spd = getY();
        return super.updateDriveSpeed(spd);
    }
        
    
    
}
