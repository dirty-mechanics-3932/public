/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dirtymechanics.frc.sensor;

import org.dirtymechanics.event.ButtonEventHandler;
import org.dirtymechanics.event.impl.ButtonListener;

/**
 *
 * @author frc
 */
public class CameraButtonEventHandler implements ButtonEventHandler {
    private Camera camera;
    boolean ledOn = false;
    
    
    
    public CameraButtonEventHandler(Camera camera) {
        this.camera = camera;
    }

    public void onEvent(int buttonEvent) {
        switch (buttonEvent) {
            case ButtonListener.SINGLE_CLICK:
                if (ledOn) {
                    camera.ledOff();
                    ledOn = false;
                } else {
                    camera.ledOn();
                    ledOn = true;
                }
                break;
            case ButtonListener.HOLD:
                break;
            case ButtonListener.NEUTRAL:
                break;
        }
    }
    
}
