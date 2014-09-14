
package org.dirtymechanics.frc.component.arm.event;

import org.dirtymechanics.event.ButtonEventHandler;
import org.dirtymechanics.event.impl.ButtonListener;
import org.dirtymechanics.frc.component.arm.Roller;
import org.dirtymechanics.frc.control.GameController;



public class RollerReverseButtonEventHandler implements ButtonEventHandler {
    private GameController controller;
    private Roller roller;
    
    public RollerReverseButtonEventHandler(GameController operatorController, Roller roller) {
        this.controller = operatorController;
        this.roller = roller;
    }

    public void onEvent(int buttonEvent) {
        switch (buttonEvent) {
            case ButtonListener.SINGLE_CLICK:
                roller.toggleReverse();
                break;
            case ButtonListener.HOLD:
                break;
            case ButtonListener.NEUTRAL:
                break;
        }
    }
    
}
