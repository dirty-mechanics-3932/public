/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dirtymechanics.frc.sensor;

import org.dirtymechanics.event.impl.ButtonListener;
import org.dirtymechanics.frc.control.DriverLeftStick;
import org.dirtymechanics.frc.util.Updatable;

/**
 *
 * @author frc
 */
public class CameraButtonListener extends ButtonListener implements Updatable {
    DriverLeftStick leftStick;
    public static final int BUTTON_11 = 11;
    
    public CameraButtonListener(DriverLeftStick leftStick) {
        this.leftStick = leftStick;
    }
    
    public void update() {
        long currentTime = System.currentTimeMillis();
        this.updateState(leftStick.getRawButton(BUTTON_11), currentTime);
    }
    
}
