
package org.dirtymechanics.frc.component.arm.event;

import org.dirtymechanics.event.ButtonEventHandler;
import org.dirtymechanics.event.impl.ButtonListener;
import org.dirtymechanics.frc.component.arm.Roller;
import org.dirtymechanics.frc.control.GameController;



public class RollerForwardButtonEventHandler implements ButtonEventHandler {
    private GameController controller;
    private Roller roller;
    
    public RollerForwardButtonEventHandler(GameController operatorController, Roller roller) {
        this.controller = operatorController;
        this.roller = roller;
    }

    public void onEvent(int buttonEvent) {
        switch (buttonEvent) {
            case ButtonListener.SINGLE_CLICK:
                roller.toggleForward();
                break;
            case ButtonListener.HOLD:
                break;
            case ButtonListener.NEUTRAL:
                break;
        }
    }
    
}
