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
public class DriverStick extends Joystick {
    protected double speed = 0;

    public DriverStick(int port) {
        super(port);
    }

    public double updateDriveSpeed(double spd) {
        double scale = -1;
        spd *= scale;
        if (spd > 0) {
            if (spd > speed) {
                speed += .0625;
            } else {
                speed = spd;
            }
        } else {
            if (spd < speed) {
                speed -= .0625;
            } else {
                speed = spd;
            }
        }
        return spd; //rightSpeed;
    }
    
    
}
