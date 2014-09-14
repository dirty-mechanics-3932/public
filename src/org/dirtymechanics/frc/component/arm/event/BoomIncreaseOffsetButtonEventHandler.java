
package org.dirtymechanics.frc.component.arm.event;

import org.dirtymechanics.event.ButtonEventHandler;
import org.dirtymechanics.event.impl.ButtonListener;
import org.dirtymechanics.frc.component.arm.PIDBoom;
import org.dirtymechanics.frc.control.OperatorJoystick;



public class BoomIncreaseOffsetButtonEventHandler implements ButtonEventHandler {
    private OperatorJoystick operatorJoy;
    private PIDBoom boom;
    
    public BoomIncreaseOffsetButtonEventHandler(OperatorJoystick operatorJoy, PIDBoom boom) {
        this.operatorJoy = operatorJoy;
        this.boom = boom;
    }

    public void onEvent(int buttonEvent) {
        switch (buttonEvent) {
            case ButtonListener.SINGLE_CLICK:
                boom.increaseOffset();
                break;
            case ButtonListener.HOLD:
                break;
            case ButtonListener.NEUTRAL:
                break;
        }
    }
    
}
