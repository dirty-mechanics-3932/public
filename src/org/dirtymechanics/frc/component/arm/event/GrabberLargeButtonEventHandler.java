
package org.dirtymechanics.frc.component.arm.event;

import org.dirtymechanics.event.ButtonEventHandler;
import org.dirtymechanics.event.impl.ButtonListener;
import org.dirtymechanics.frc.component.arm.grabber.Grabber;
import org.dirtymechanics.frc.control.GameController;



public class GrabberLargeButtonEventHandler implements ButtonEventHandler {
    private GameController controller;
    private Grabber grabber;
    
    public GrabberLargeButtonEventHandler(GameController operatorController, Grabber roller) {
        this.controller = operatorController;
        this.grabber = roller;
    }

    public void onEvent(int buttonEvent) {
        switch (buttonEvent) {
            case ButtonListener.SINGLE_CLICK:
                if (grabber.isOpenLarge()) {
                    grabber.closeLarge();
                } else {
                    grabber.openLarge();
                }
                break;
            case ButtonListener.HOLD:
                break;
            case ButtonListener.NEUTRAL:
                break;
        }
    }
    
}
