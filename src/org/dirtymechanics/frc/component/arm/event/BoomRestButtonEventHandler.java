
package org.dirtymechanics.frc.component.arm.event;

import org.dirtymechanics.event.ButtonEventHandler;
import org.dirtymechanics.event.impl.ButtonListener;
import org.dirtymechanics.frc.component.arm.PIDBoom;
import org.dirtymechanics.frc.control.OperatorGameController;
import org.dirtymechanics.frc.control.OperatorJoystick;



public class BoomRestButtonEventHandler implements ButtonEventHandler {
    private OperatorGameController operatorGameController;
    private PIDBoom boom;
    
    public BoomRestButtonEventHandler(OperatorGameController operatorGameController, PIDBoom boom) {
        this.operatorGameController = operatorGameController;
        this.boom = boom;
    }

    public void onEvent(int buttonEvent) {
        switch (buttonEvent) {
            case ButtonListener.SINGLE_CLICK:
                boom.rest();
                break;
            case ButtonListener.HOLD:
                break;
            case ButtonListener.NEUTRAL:
                break;
        }
    }
    
}
