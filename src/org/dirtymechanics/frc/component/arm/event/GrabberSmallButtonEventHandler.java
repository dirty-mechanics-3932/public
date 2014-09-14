
package org.dirtymechanics.frc.component.arm.event;

import org.dirtymechanics.event.ButtonEventHandler;
import org.dirtymechanics.event.impl.ButtonListener;
import org.dirtymechanics.frc.component.arm.grabber.Grabber;
import org.dirtymechanics.frc.control.GameController;



public class GrabberSmallButtonEventHandler implements ButtonEventHandler {
    private GameController controller;
    private Grabber grabber;
    
    public GrabberSmallButtonEventHandler(GameController operatorController, Grabber roller) {
        this.controller = operatorController;
        this.grabber = roller;
    }

    public void onEvent(int buttonEvent) {
        switch (buttonEvent) {
            case ButtonListener.SINGLE_CLICK:
                grabber.openSmall();
                break;
            case ButtonListener.HOLD:
                break;
            case ButtonListener.NEUTRAL:
                break;
        }
    }
    
}
